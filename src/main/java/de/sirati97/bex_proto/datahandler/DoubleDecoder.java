package de.sirati97.bex_proto.datahandler;

import de.sirati97.bex_proto.util.CursorByteBuffer;

public class DoubleDecoder extends DecoderBase<Double> {

	@Override
	public Double decode(CursorByteBuffer dat, boolean header) {
		return BExStatic.getDouble(dat);
	}


}
