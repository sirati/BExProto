package de.sirati97.bex_proto.datahandler;

import de.sirati97.bex_proto.util.bytebuffer.ByteBuffer;

import java.net.InetAddress;

public class InetAddressStream implements Stream {
	private InetAddress data;
	
	public InetAddressStream(InetAddress data) {
		this.data = data;
	}

	@Override
	public ByteBuffer getBytes() {
		return BExStatic.setByteArray(data.getAddress());
	}

}
