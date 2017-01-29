package de.sirati97.bex_proto.datahandler;

import de.sirati97.bex_proto.util.CursorByteBuffer;

public class LongDecoder implements IDecoder<Long> {

	@Override
	public Long decode(CursorByteBuffer dat) {
		return BExStatic.getLong(dat);
	}

}
