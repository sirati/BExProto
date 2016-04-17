package de.sirati97.bex_proto.datahandler;

import de.sirati97.bex_proto.util.CursorByteBuffer;

public class LongExtractor implements StreamExtractor<Long> {

	@Override
	public Long extract(CursorByteBuffer dat) {
		return BExStatic.getLong(dat);
	}

}
