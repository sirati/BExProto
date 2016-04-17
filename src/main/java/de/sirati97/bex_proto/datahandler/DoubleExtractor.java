package de.sirati97.bex_proto.datahandler;

import de.sirati97.bex_proto.util.CursorByteBuffer;

public class DoubleExtractor implements StreamExtractor<Double> {

	@Override
	public Double extract(CursorByteBuffer dat) {
		return BExStatic.getDouble(dat);
	}


}
