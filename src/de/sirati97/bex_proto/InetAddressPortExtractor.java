package de.sirati97.bex_proto;

import java.net.InetAddress;

public class InetAddressPortExtractor implements
		StreamExtractor<InetAddressPort> {

	@Override
	public InetAddressPort extract(ExtractorDat dat) {
		InetAddress address = (InetAddress) Type.InetAddress.getExtractor().extract(dat);
		int port = (Integer) Type.Integer.getExtractor().extract(dat);
		
		return new InetAddressPort(address, port);
	}

}
