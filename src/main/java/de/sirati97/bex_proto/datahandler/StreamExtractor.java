package de.sirati97.bex_proto.datahandler;

import de.sirati97.bex_proto.util.CursorByteBuffer;

public interface StreamExtractor<e> {
	public e extract(CursorByteBuffer dat);
}
