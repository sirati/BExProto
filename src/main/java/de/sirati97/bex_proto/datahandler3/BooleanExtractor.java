package de.sirati97.bex_proto.datahandler;

import de.sirati97.bex_proto.util.ByteBuffer;

public class BooleanExtractor implements StreamExtractor<Boolean> {

	@Override
	public Boolean extract(ByteBuffer dat) {
		return BExStatic.getBoolean(dat);
	}


}
