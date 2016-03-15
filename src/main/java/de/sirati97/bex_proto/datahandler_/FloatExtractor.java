package de.sirati97.bex_proto.datahandler_;

import de.sirati97.bex_proto.util.ByteBuffer;

public class FloatExtractor implements StreamExtractor<Float> {

	@Override
	public Float extract(ByteBuffer dat) {
		return BExStatic.getFloat(dat);
	}


}
