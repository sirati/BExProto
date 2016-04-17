package de.sirati97.bex_proto.datahandler;

import de.sirati97.bex_proto.util.CursorByteBuffer;

public class FloatExtractor implements StreamExtractor<Float> {

	@Override
	public Float extract(CursorByteBuffer dat) {
		return BExStatic.getFloat(dat);
	}


}
