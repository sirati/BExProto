package de.sirati97.bex_proto.datahandler;

import de.sirati97.bex_proto.util.CursorByteBuffer;

public class IntegerDecoder extends DecoderBase<Integer> {


	@Override
	public Integer decode(CursorByteBuffer dat, boolean header) {
		return BExStatic.getInteger(dat);
	}

}
