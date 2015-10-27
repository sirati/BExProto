package de.sirati97.bex_proto.network;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;

import javax.crypto.Cipher;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSocket;

import de.sirati97.bex_proto.StreamReader;
import de.sirati97.bex_proto.network.AsyncHelper.AsyncTask;
import de.sirati97.bex_proto.util.exception.NotImplementedException;

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
	private Cipher writeCipher;
	private ISocketFactory socketFactory;
	
	public NetConnection(AsyncHelper asyncHelper, Socket socket,
			NetConnectionManager netConnectionManager, StreamReader streamReader, NetCreator creator, ISocketFactory socketFactory, Cipher writeCipher) {
		this.asyncHelper = asyncHelper;
		this.socket = socket;
		this.netConnectionManager = netConnectionManager;
		this.streamReader = streamReader;
		this.creator = creator==null?this:creator;
		this.writeCipher = writeCipher;
		this.socketFactory = socketFactory;
	}

	public void start() {
		if (enabled)
			return;
		enabled = true;
		netConnectionManager.add(this);
		if (socket instanceof SSLSocket) {
			try {
				((SSLSocket) socket).startHandshake();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		readerTask = asyncHelper.runAsync(new Runnable() {
			public void run() {
				byte[] overflow=null;
				try {
					InputStream in = socketFactory.getSocketInputStream(socket);
					while (enabled && !Thread.interrupted()) {
						try {
							if (!isReadingLocked() && (socket.isClosed() || in.available() > 0)) {
								if (socket.isClosed()) {
									stop();
									return;
								}
								int available = in.available();
								byte[] buffer = new byte[available];
								in.read(buffer);
								if (overflow != null) {
									byte[] buffer2 = new byte[overflow.length + buffer.length];
									System.arraycopy(overflow, 0, buffer2, 0, overflow.length);
									System.arraycopy(buffer, 0, buffer2, overflow.length, buffer.length);
									buffer = buffer2;
									overflow = null;
								}
								overflow = streamReader.read(buffer, NetConnection.this, asyncHelper, "Stream Exercuter Thread for " + socket.getInetAddress().getHostAddress() + ":" + socket.getPort());
							} else {
								Thread.sleep(0, 1);
							}
						} catch(SSLException e) {
							stop();
						} catch (IOException e) {
							e.printStackTrace();
						} catch (InterruptedException e) {
						}

					}
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}, "Socket Reader Thread for " + socket.getInetAddress().getHostAddress() + ":" + socket.getPort());
	}
	
	public synchronized void send(byte[] stream) {
		if (socket.isClosed()) {
			stop();
			return;
		}
		
		try {
			socket.getOutputStream().write(stream);
			socket.getOutputStream().flush();
			
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
	
	
	private long pingTimestamp=0;
	private long pingSendTimestamp=0;
	
	private int ping=-1;
	public void receivedPong(int ping) {
		this.ping = ping;
		this.pingTimestamp = System.currentTimeMillis();
	}
	
	public synchronized int getPingMicro() {
		long currentTimestamp = System.currentTimeMillis();
		
		while (currentTimestamp/1000>pingTimestamp/1000) {
			if (currentTimestamp/100>pingSendTimestamp/100) {
				_sendPing();
			}
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return ping;
	}
	

	public int getPing() {
		return getPingMicro()/1000;
	}
	private void _sendPing() {
		pingSendTimestamp = System.currentTimeMillis();
		getCreator().sendPing(this);
	}
	
	public void sendPing(NetConnection connection) {
		throw new NotImplementedException("Ping is not supported by this connection");
	}

	public void stop() {
		if (stoped)return;
		synchronized (this) {
			stoped = true;
			enabled = false;
			readerTask.stop();
//			try {
//				socket.shutdownOutput();
//			} catch (IOException e) {}
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
	
	public Cipher getWriteCipher() {
		return writeCipher;
	}
	
}
