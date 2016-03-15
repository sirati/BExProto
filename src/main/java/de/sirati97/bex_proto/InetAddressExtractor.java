package de.sirati97.bex_proto;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class InetAddressExtractor implements StreamExtractor<InetAddress> {

	@Override
	public InetAddress extract(ExtractorDat dat) {
		try {
			return InetAddress.getByAddress(dat.getMulti(4));
		} catch (UnknownHostException e) {
			throw new IllegalArgumentException(e);
		}
	}

}
