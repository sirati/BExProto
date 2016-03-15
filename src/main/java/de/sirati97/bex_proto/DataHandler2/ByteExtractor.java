package de.sirati97.bex_proto.DataHandler2;

import de.sirati97.bex_proto.util.ByteBuffer;

public class ByteExtractor implements StreamExtractor<Byte> {

	@Override
	public Byte extract(ByteBuffer dat) {
		return BExStatic.getByte(dat);
	}

}
