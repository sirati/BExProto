package de.sirati97.bex_proto.datahandler_;

import de.sirati97.bex_proto.util.ByteBuffer;

public class DoubleExtractor implements StreamExtractor<Double> {

	@Override
	public Double extract(ByteBuffer dat) {
		return BExStatic.getDouble(dat);
	}


}
