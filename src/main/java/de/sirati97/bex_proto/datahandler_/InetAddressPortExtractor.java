package de.sirati97.bex_proto.datahandler_;

import de.sirati97.bex_proto.util.ByteBuffer;

import java.net.InetAddress;

public class InetAddressPortExtractor implements
		StreamExtractor<InetAddressPort> {

	@Override
	public InetAddressPort extract(ByteBuffer dat) {
		InetAddress address = (InetAddress) Type.InetAddress.getExtractor().extract(dat);
		int port = (Integer) Type.Integer.getExtractor().extract(dat);
		
		return new InetAddressPort(address, port);
	}

}
