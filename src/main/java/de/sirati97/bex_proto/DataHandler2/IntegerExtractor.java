package de.sirati97.bex_proto.DataHandler2;

import de.sirati97.bex_proto.util.ByteBuffer;

public class IntegerExtractor implements StreamExtractor<Integer> {


	@Override
	public Integer extract(ByteBuffer dat) {
		return BExStatic.getInteger(dat);
	}

}
