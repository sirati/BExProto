package de.sirati97.bex_proto.network;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class SocketFactory implements ISocketFactory {

	@Override
	public Socket createSocket(String host, int port) throws UnknownHostException, IOException {
		return new Socket(host, port);
	}

	@Override
	public ServerSocket createServerSocket(int port) throws IOException {
		return new ServerSocket(port);
	}

	@Override
	public ServerSocket createServerSocket(int port, InetAddress address) throws IOException {
		return new ServerSocket(port, -1 , address);
	}

	@Override
	public InputStream createSocketInputStream(Socket socket) throws IOException {
		return socket.getInputStream();
	}

	@Override
	public boolean isSocketInputStream(Socket socket, InputStream in) throws IOException {
		return socket.getInputStream().equals(in);
	}

	@Override
	public void switchInputStream(InputStream inOld, InputStream inNew) {}

}
