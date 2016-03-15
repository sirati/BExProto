package de.sirati97.bex_proto.datahandler;

import java.net.InetAddress;
import java.net.InetSocketAddress;

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

	public InetSocketAddress toInetSocketAddress() {
		return new InetSocketAddress(getInetAddress(), getPort());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof InetAddressPort) {
			return getInetAddress().equals(((InetAddressPort) obj).getInetAddress()) && getPort()==((InetAddressPort) obj).getPort();
		}
		return super.equals(obj);
	}
}
