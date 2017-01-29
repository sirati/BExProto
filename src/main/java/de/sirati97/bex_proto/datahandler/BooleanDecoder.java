package de.sirati97.bex_proto.datahandler;

import de.sirati97.bex_proto.util.CursorByteBuffer;

public class BooleanDecoder implements IDecoder<Boolean> {

	@Override
	public Boolean decode(CursorByteBuffer dat) {
		return BExStatic.getBoolean(dat);
	}


}
