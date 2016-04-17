package de.sirati97.bex_proto.datahandler;

import de.sirati97.bex_proto.util.CursorByteBuffer;

public class IntegerExtractor implements StreamExtractor<Integer> {


	@Override
	public Integer extract(CursorByteBuffer dat) {
		return BExStatic.getInteger(dat);
	}

}
