package de.sirati97.bex_proto.network;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.HashSet;
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
	private InetAddress address;
	
	public NetServer(AsyncHelper asyncHelper, int port, StreamReader streamReader) {
		this(asyncHelper, port, null, streamReader);
	}
	
	public NetServer(AsyncHelper asyncHelper, int port, InetAddress address, StreamReader streamReader) {
		this.asyncHelper = asyncHelper;
		this.port = port;
		this.streamReader = streamReader;
		this.address = address;
	}
	
	
	public void start() {
		if (enabled)return;
		enabled = true;
		try {
			serverSocket = address==null?new ServerSocket(port):new ServerSocket(port, -1, address);
			System.out.println("Starting server on port " + port);
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
			}, "Server Listener Thread port=" + port);
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
		for (NetConnection connection:new HashSet<>(getConnections())) {
			connection.stop();
		}
		try {
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
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
