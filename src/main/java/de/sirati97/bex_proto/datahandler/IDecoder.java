package de.sirati97.bex_proto.datahandler;

import de.sirati97.bex_proto.util.CursorByteBuffer;

public interface IDecoder<Type> {
	public Type decode(CursorByteBuffer dat);
}
