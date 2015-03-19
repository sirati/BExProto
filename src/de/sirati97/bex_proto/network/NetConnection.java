package de.sirati97.bex_proto.network;

import java.io.IOException;
import java.net.Socket;

import de.sirati97.bex_proto.StreamReader;
import de.sirati97.bex_proto.network.AsyncHelper.AsyncTask;

public class NetConnection {
	private AsyncHelper asyncHelper;
	private Socket socket;
	private boolean enabled = false;
	private NetConnectionManager netConnectionManager;
	private StreamReader streamReader;
	private AsyncTask readerTask;
	
	public NetConnection(AsyncHelper asyncHelper, Socket socket,
			NetConnectionManager netConnectionManager, StreamReader streamReader) {
		this.asyncHelper = asyncHelper;
		this.socket = socket;
		this.netConnectionManager = netConnectionManager;
		this.streamReader = streamReader;
	}

	public void start() {
		if (enabled)
			return;
		enabled = true;
		netConnectionManager.add(this);
		readerTask = asyncHelper.runAsync(new Runnable() {
			public void run() {
				while (enabled) {
					try {
						if (socket.getInputStream().available() > 0) {
							int available = socket.getInputStream().available();
							final byte[] buffer = new byte[available];
							socket.getInputStream().read(buffer);
							asyncHelper.runAsync(new Runnable() {
								public void run() {
									streamReader.read(buffer, NetConnection.this);
								}
							});

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
		});
	}

	public void send(byte[] stream) {
		try {
			socket.getOutputStream().write(stream);
		} catch (IOException e) {
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
		enabled = false;
		readerTask.stop();
	}
	
	protected StreamReader getStreamReader() {
		return streamReader;
	}

}
