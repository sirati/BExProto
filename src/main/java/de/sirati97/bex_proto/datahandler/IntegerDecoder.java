package de.sirati97.bex_proto.datahandler;

import de.sirati97.bex_proto.util.CursorByteBuffer;

public class IntegerDecoder implements IDecoder<Integer> {


	@Override
	public Integer decode(CursorByteBuffer dat) {
		return BExStatic.getInteger(dat);
	}

}
