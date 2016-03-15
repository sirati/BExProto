package de.sirati97.bex_proto.datahandler;

import de.sirati97.bex_proto.util.ByteBuffer;

public class LongExtractor implements StreamExtractor<Long> {

	@Override
	public Long extract(ByteBuffer dat) {
		return BExStatic.getLong(dat);
	}

}
