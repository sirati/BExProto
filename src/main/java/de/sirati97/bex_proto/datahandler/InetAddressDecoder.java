package de.sirati97.bex_proto.datahandler;

import de.sirati97.bex_proto.util.CursorByteBuffer;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class InetAddressDecoder extends DecoderBase<InetAddress> {

	@Override
	public InetAddress decode(CursorByteBuffer dat, boolean header) {
		try {
			int length = (Integer) Types.Integer.getDecoder().decode(dat);
			return InetAddress.getByAddress(dat.getMulti(length));
		} catch (UnknownHostException e) {
			throw new IllegalArgumentException(e);
		}
	}

}
