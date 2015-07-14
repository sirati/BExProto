package de.sirati97.bex_proto.network;

import java.io.IOException;
import java.net.Socket;

import de.sirati97.bex_proto.StreamReader;

public class NetClient extends NetConnection {
	private String ip;
	private int port;
	
	public NetClient(AsyncHelper asyncHelper, String ip, int port, StreamReader streamReader) {
		super(asyncHelper, null, new NetConnectionManager(), streamReader, null);
		this.ip = ip;
		this.port = port;
	}
	
	
	@Override
	public void start() {
		if (isEnabled()) return;

		try {
			Socket socket = new Socket(ip, port);
			setSocket(socket);
			super.start();
		} catch (IOException e) {
			setEnabled(false);
			throw new IllegalStateException(e);
		}
		
	}

}
