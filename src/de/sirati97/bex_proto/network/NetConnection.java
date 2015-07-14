package de.sirati97.bex_proto.network;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;

import de.sirati97.bex_proto.StreamReader;
import de.sirati97.bex_proto.network.AsyncHelper.AsyncTask;

public class NetConnection implements NetCreator {
	private AsyncHelper asyncHelper;
	private Socket socket;
	private boolean enabled = false;
	private NetConnectionManager netConnectionManager;
	private StreamReader streamReader;
	private AsyncTask readerTask;
	private NetCreator creator;
	private boolean readingLocked = false;
	private boolean registered = true;
	private boolean stoped = false;
	
	public NetConnection(AsyncHelper asyncHelper, Socket socket,
			NetConnectionManager netConnectionManager, StreamReader streamReader, NetCreator creator) {
		this.asyncHelper = asyncHelper;
		this.socket = socket;
		this.netConnectionManager = netConnectionManager;
		this.streamReader = streamReader;
		this.creator = creator==null?this:creator;
	}

	public void start() {
		if (enabled)
			return;
		enabled = true;
		netConnectionManager.add(this);
		readerTask = asyncHelper.runAsync(new Runnable() {
			public void run() {
				byte[] overflow=null;
				while (enabled && !Thread.interrupted()) {
					try {
						if (!isReadingLocked() && (socket.isClosed() || socket.getInputStream().available() > 0)) {
							if (socket.isClosed()) {
								stop();
								return;
							}
							int available = socket.getInputStream().available();
							byte[] buffer = new byte[available];
							socket.getInputStream().read(buffer);
							if (overflow != null) {
								byte[] buffer2 = new byte[overflow.length + buffer.length];
								System.arraycopy(overflow, 0, buffer2, 0, overflow.length);
								System.arraycopy(buffer, 0, buffer2, overflow.length, buffer.length);
								buffer = buffer2;
								overflow = null;
							}
							overflow=streamReader.read(buffer, NetConnection.this, asyncHelper, "Stream Exercuter Thread for " + socket.getInetAddress().getHostAddress() + ":" + socket.getPort());
						} else {
							Thread.sleep(0, 1);
						}
					} catch (IOException e) {
						e.printStackTrace();
					} catch (InterruptedException e) {
					}

				}
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}, "Socket Reader Thread for " + socket.getInetAddress().getHostAddress() + ":" + socket.getPort());
	}

	public void send(byte[] stream) {
		if (socket.isClosed()) {
			stop();
			return;
		}
		
		try {
			socket.getOutputStream().write(stream);
		} catch (SocketException e) {
			if (!registered)e.printStackTrace();
			stop();
			return;
		}	catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Socket getSocket() {
		return socket;
	}

	protected void setSocket(Socket socket) {
		this.socket = socket;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void stop() {
		if (stoped)return;
		synchronized (this) {
			stoped = true;
			enabled = false;
			readerTask.stop();
			try {
				socket.shutdownOutput();
			} catch (IOException e) {}
			getCreator().onSocketClosed(this);
		}
		
	}
	
	public int getPort() {
		return socket.getPort();
	}
	
	protected StreamReader getStreamReader() {
		return streamReader;
	}
	
	public NetCreator getCreator() {
		return creator;
	}
	
	protected void setCreator(NetCreator creator) {
		this.creator = creator;
	}

	@Override
	public void onSocketClosed(NetConnection connection) {}

	public boolean isReadingLocked() {
		return readingLocked;
	}
	
	public void setReadingLocked(boolean readingLocked) {
		this.readingLocked = readingLocked;
	}
	
	public boolean isRegistered() {
		return registered;
	}
	
	public void setRegistered(boolean registered) {
		this.registered = registered;
	}
	
	public InetAddress getInetAddress() {
		return socket.getInetAddress();
	}
	
	protected void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	public String toString() {
		return super.toString() + "{ip=" + socket.getInetAddress().getHostAddress() + ",port=" + socket.getPort() + "}";
	}
	
}
