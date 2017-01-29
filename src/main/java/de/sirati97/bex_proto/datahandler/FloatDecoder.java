package de.sirati97.bex_proto.datahandler;

import de.sirati97.bex_proto.util.CursorByteBuffer;

public class FloatDecoder implements IDecoder<Float> {

	@Override
	public Float decode(CursorByteBuffer dat) {
		return BExStatic.getFloat(dat);
	}


}
