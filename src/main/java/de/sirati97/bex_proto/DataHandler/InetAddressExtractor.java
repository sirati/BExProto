package de.sirati97.bex_proto.DataHandler;

import de.sirati97.bex_proto.util.ByteBuffer;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class InetAddressExtractor implements StreamExtractor<InetAddress> {

	@Override
	public InetAddress extract(ByteBuffer dat) {
		try {
			return InetAddress.getByAddress(dat.getMulti(4));
		} catch (UnknownHostException e) {
			throw new IllegalArgumentException(e);
		}
	}

}
