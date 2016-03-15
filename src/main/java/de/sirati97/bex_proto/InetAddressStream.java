package de.sirati97.bex_proto;

import java.net.InetAddress;

public class InetAddressStream implements Stream {
	private InetAddress data;
	
	public InetAddressStream(InetAddress data) {
		this.data = data;
	}

	@Override
	public byte[] getBytes() {
		return data.getAddress();
	}

}
