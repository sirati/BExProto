package de.sirati97.bex_proto.DataHandler2;

import de.sirati97.bex_proto.util.ByteBuffer;

public class BooleanExtractor implements StreamExtractor<Boolean> {

	@Override
	public Boolean extract(ByteBuffer dat) {
		return BExStatic.getBoolean(dat);
	}


}
