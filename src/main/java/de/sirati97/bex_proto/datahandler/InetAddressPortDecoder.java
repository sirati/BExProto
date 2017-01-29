package de.sirati97.bex_proto.datahandler;

import de.sirati97.bex_proto.util.CursorByteBuffer;

import java.net.InetAddress;

public class InetAddressPortDecoder implements
        IDecoder<InetAddressPort> {

	@Override
	public InetAddressPort decode(CursorByteBuffer dat) {
		InetAddress address = (InetAddress) Type.InetAddress.getDecoder().decode(dat);
		int port = (Integer) Type.Integer.getDecoder().decode(dat);
		
		return new InetAddressPort(address, port);
	}

}
