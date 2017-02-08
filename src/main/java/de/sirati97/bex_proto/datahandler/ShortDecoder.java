package de.sirati97.bex_proto.datahandler;

import de.sirati97.bex_proto.util.CursorByteBuffer;

public class ShortDecoder extends DecoderBase<Short> {

	@Override
	public Short decode(CursorByteBuffer dat, boolean header) {
		return BExStatic.getShort(dat);
	}

}
