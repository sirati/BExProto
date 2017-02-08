package de.sirati97.bex_proto.datahandler;

import de.sirati97.bex_proto.util.CursorByteBuffer;

public interface IDecoder<Type> {
	Type decode(CursorByteBuffer dat, boolean header);
	Type decode(CursorByteBuffer dat);
}
