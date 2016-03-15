package de.sirati97.bex_proto.DataHandler2;

import de.sirati97.bex_proto.util.ByteBuffer;

public class ShortExtractor implements StreamExtractor<Short> {

	@Override
	public Short extract(ByteBuffer dat) {
		return BExStatic.getShort(dat);
	}

}
