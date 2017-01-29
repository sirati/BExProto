package de.sirati97.bex_proto.datahandler;

import de.sirati97.bex_proto.util.CursorByteBuffer;

public class ByteDecoder implements IDecoder<Byte> {

	@Override
	public Byte decode(CursorByteBuffer dat) {
		return BExStatic.getByte(dat);
	}

}
