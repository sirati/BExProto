package de.sirati97.bex_proto.datahandler;

import de.sirati97.bex_proto.util.CursorByteBuffer;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class InetAddressExtractor implements StreamExtractor<InetAddress> {

	@Override
	public InetAddress extract(CursorByteBuffer dat) {
		try {
			int length = (Integer) Type.Integer.getExtractor().extract(dat);
			return InetAddress.getByAddress(dat.getMulti(length));
		} catch (UnknownHostException e) {
			throw new IllegalArgumentException(e);
		}
	}

}
