package de.sirati97.bex_proto;

import java.net.InetAddress;

public class InetAddressPort {
	private InetAddress inetAddress;
	private int port;
	
	public InetAddressPort(InetAddress inetAddress, int port) {
		this.inetAddress = inetAddress;
		this.port = port;
	}
	
	public InetAddress getInetAddress() {
		return inetAddress;
	}
	
	public int getPort() {
		return port;
	}

}
