package de.sirati97.bex_proto.datahandler;

import de.sirati97.bex_proto.util.CursorByteBuffer;

public class FloatDecoder extends DecoderBase<Float> {

	@Override
	public Float decode(CursorByteBuffer dat, boolean header) {
		return BExStatic.getFloat(dat);
	}


}
