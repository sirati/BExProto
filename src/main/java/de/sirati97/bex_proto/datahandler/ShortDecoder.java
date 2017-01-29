package de.sirati97.bex_proto.datahandler;

import de.sirati97.bex_proto.util.CursorByteBuffer;

public class ShortDecoder implements IDecoder<Short> {

	@Override
	public Short decode(CursorByteBuffer dat) {
		return BExStatic.getShort(dat);
	}

}
