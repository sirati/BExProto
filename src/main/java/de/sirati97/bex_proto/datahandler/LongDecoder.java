package de.sirati97.bex_proto.datahandler;

import de.sirati97.bex_proto.util.CursorByteBuffer;

public class LongDecoder extends DecoderBase<Long> {

	@Override
	public Long decode(CursorByteBuffer dat, boolean header) {
		return BExStatic.getLong(dat);
	}

}
