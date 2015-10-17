package de.sirati97.bex_proto.network;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.Set;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import org.apache.commons.lang3.NotImplementedException;

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
	private Cipher readCipher;
	private Cipher writeCipher;
	private ISocketFactory socketFactory;
	
	public NetServer(AsyncHelper asyncHelper, int port, StreamReader streamReader, ISocketFactory socketFactory, SecretKey secretKey) {
		this(asyncHelper, port, null, streamReader, socketFactory, secretKey);
	}
	
	public NetServer(AsyncHelper asyncHelper, int port, InetAddress address, StreamReader streamReader, ISocketFactory socketFactory, SecretKey secretKey) {
		this.asyncHelper = asyncHelper;
		this.port = port;
		this.streamReader = streamReader;
		this.address = address;
		this.socketFactory = socketFactory;
		if (secretKey != null) {
			try {
				this.readCipher = Cipher.getInstance(secretKey.getAlgorithm());
				this.readCipher.init(Cipher.DECRYPT_MODE, secretKey);
				this.writeCipher = Cipher.getInstance(secretKey.getAlgorithm());
				this.writeCipher.init(Cipher.ENCRYPT_MODE, secretKey);
				streamReader.setCipher(readCipher);
			} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) {
				throw new IllegalStateException(e);
			}
		}
	}
	
	public void sendPing(NetConnection connection) {
		throw new NotImplementedException("Ping is not supported by this connection");
	}
	
	public void start() {
		if (enabled)return;
		enabled = true;
		try {
			serverSocket = address==null?socketFactory.createServerSocket(port):socketFactory.createServerSocket(port, address);
			System.out.println("Starting server on port " + port);
			readerTask = asyncHelper.runAsync(new Runnable() {
				public void run() {
					while (enabled && !Thread.interrupted()) {
						Socket socket = null;
						try {
							if (serverSocket.isClosed()) {
								System.out.println("The socket was closed!");
							} else if ((socket = serverSocket.accept()) != null) {
								NetConnection connection = new NetConnection(asyncHelper, socket, netConnectionManager, streamReader, NetServer.this, socketFactory, writeCipher);
								System.out.println("Connected at " + connection.getInetAddress().getHostAddress() + ":" +  connection.getPort());
								onPreConnected(connection);
								connection.start();
								onConnected(connection);
							}
						} catch (IOException e) {
							if (!(e instanceof java.net.SocketException) && !enabled) {
								e.printStackTrace();
							}
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
			serverSocket.setSoTimeout(0);
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
