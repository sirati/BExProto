package de.sirati97.bex_proto.datahandler;

import de.sirati97.bex_proto.util.ByteBuffer;

public class ShortExtractor implements StreamExtractor<Short> {

	@Override
	public Short extract(ByteBuffer dat) {
		return BExStatic.getShort(dat);
	}

}
