package de.sirati97.bex_proto.datahandler;

import de.sirati97.bex_proto.util.CursorByteBuffer;

public class ShortExtractor implements StreamExtractor<Short> {

	@Override
	public Short extract(CursorByteBuffer dat) {
		return BExStatic.getShort(dat);
	}

}
