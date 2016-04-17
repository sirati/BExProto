package de.sirati97.bex_proto.datahandler;

import de.sirati97.bex_proto.util.CursorByteBuffer;

public class BooleanExtractor implements StreamExtractor<Boolean> {

	@Override
	public Boolean extract(CursorByteBuffer dat) {
		return BExStatic.getBoolean(dat);
	}


}
