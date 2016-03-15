package de.sirati97.bex_proto.datahandler_;

import de.sirati97.bex_proto.util.ByteBuffer;

public class IntegerExtractor implements StreamExtractor<Integer> {


	@Override
	public Integer extract(ByteBuffer dat) {
		return BExStatic.getInteger(dat);
	}

}
