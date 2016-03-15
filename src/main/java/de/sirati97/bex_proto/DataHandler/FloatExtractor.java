package de.sirati97.bex_proto.DataHandler;

import de.sirati97.bex_proto.util.ByteBuffer;

public class FloatExtractor implements StreamExtractor<Float> {

	@Override
	public Float extract(ByteBuffer dat) {
		return BExStatic.getFloat(dat);
	}


}
