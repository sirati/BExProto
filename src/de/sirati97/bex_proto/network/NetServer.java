package de.sirati97.bex_proto.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Set;

import de.sirati97.bex_proto.StreamReader;
import de.sirati97.bex_proto.network.AsyncHelper.AsyncTask;

public class NetServer implements NetCreator{
	private AsyncHelper asyncHelper;
	private int port;
	private ServerSocket serverSocket;
	private boolean enabled = false;
	private NetConnectionManager netConnectionManager = new NetConnectionManager();
	private StreamReader streamReader;
	private AsyncTask readerTask;
	
	public NetServer(AsyncHelper asyncHelper, int port, StreamReader streamReader) {
		this.asyncHelper = asyncHelper;
		this.port = port;
		this.streamReader = streamReader;
	}
	
	public void start() {
		if (enabled)return;
		enabled = true;
		try {
			serverSocket = new ServerSocket(port);
			readerTask = asyncHelper.runAsync(new Runnable() {
				public void run() {
					while (enabled && !Thread.interrupted()) {
						Socket socket = null;
						try {
							if (serverSocket.isClosed()) {
								System.out.println("The socket was closed!");
							} else if ((socket = serverSocket.accept()) != null) {
								NetConnection connection = new NetConnection(asyncHelper, socket, netConnectionManager, streamReader, NetServer.this);
								System.out.println("Connected at " + connection.getInetAddress().getHostAddress() + ":" +  connection.getPort());
								onPreConnected(connection);
								connection.start();
								onConnected(connection);
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
					}

					try {
						serverSocket.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	

	public boolean isEnabled() {
		return enabled;
	}
	
	public void stop() {
		enabled = false;
		readerTask.stop();
		for (NetConnection connection:getConnections()) {
			connection.stop();
		}
	}
	
	public Set<NetConnection> getConnections() {
		return netConnectionManager.getConnections();
	}
	
	protected StreamReader getStreamReader() {
		return streamReader;
	}
	
	protected void onConnected(NetConnection connection) {}
	protected void onPreConnected(NetConnection connection) {}
	

	@Override
	public void onSocketClosed(NetConnection connection) {
		netConnectionManager.remove(connection);
		
	}
	
}
