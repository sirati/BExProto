package de.sirati97.bex_proto.datahandler;

import de.sirati97.bex_proto.util.CursorByteBuffer;

public class ByteExtractor implements StreamExtractor<Byte> {

	@Override
	public Byte extract(CursorByteBuffer dat) {
		return BExStatic.getByte(dat);
	}

}
