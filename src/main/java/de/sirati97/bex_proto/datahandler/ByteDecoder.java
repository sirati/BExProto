package de.sirati97.bex_proto.datahandler;

import de.sirati97.bex_proto.util.CursorByteBuffer;

public class ByteDecoder extends DecoderBase<Byte> {

	@Override
	public Byte decode(CursorByteBuffer dat, boolean header) {
		return BExStatic.getByte(dat);
	}

}
