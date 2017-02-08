package de.sirati97.bex_proto.datahandler;

import de.sirati97.bex_proto.util.CursorByteBuffer;

public class BooleanDecoder extends DecoderBase<Boolean> {

	@Override
	public Boolean decode(CursorByteBuffer dat, boolean header) {
		return BExStatic.getBoolean(dat);
	}


}
