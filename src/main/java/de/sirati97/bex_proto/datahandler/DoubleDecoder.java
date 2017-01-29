package de.sirati97.bex_proto.datahandler;

import de.sirati97.bex_proto.util.CursorByteBuffer;

public class DoubleDecoder implements IDecoder<Double> {

	@Override
	public Double decode(CursorByteBuffer dat) {
		return BExStatic.getDouble(dat);
	}


}
