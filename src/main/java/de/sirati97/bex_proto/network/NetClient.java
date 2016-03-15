package de.sirati97.bex_proto.network;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import de.sirati97.bex_proto.StreamReader;

public class NetClient extends NetConnection {
	private String ip;
	private int port;
	private ISocketFactory socketFactory;
	
	public NetClient(AsyncHelper asyncHelper, String ip, int port, StreamReader streamReader, ISocketFactory socketFactory) {
		super(asyncHelper, null, new NetConnectionManager(), streamReader, null, socketFactory);
		this.ip = ip;
		this.port = port;
		this.socketFactory = socketFactory;
		
	}
	
	
	@Override
	public void start() {
		if (isEnabled()) return;

		try {
			Socket socket = createSocket();
			setSocket(socket);
			super.start();
		} catch (IOException e) {
			setEnabled(false);
			throw new IllegalStateException(e);
		}
		
	}

	protected Socket createSocket() throws UnknownHostException, IOException {
		return socketFactory.createSocket(ip, port);
	}
	
	@Override
	public int getPort() {
		return getSocket().getLocalPort();
	}
	
}
