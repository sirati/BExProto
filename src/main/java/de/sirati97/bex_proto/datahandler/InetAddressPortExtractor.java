package de.sirati97.bex_proto.datahandler;

import de.sirati97.bex_proto.util.CursorByteBuffer;

import java.net.InetAddress;

public class InetAddressPortExtractor implements
		StreamExtractor<InetAddressPort> {

	@Override
	public InetAddressPort extract(CursorByteBuffer dat) {
		InetAddress address = (InetAddress) Type.InetAddress.getExtractor().extract(dat);
		int port = (Integer) Type.Integer.getExtractor().extract(dat);
		
		return new InetAddressPort(address, port);
	}

}
