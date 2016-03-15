package de.sirati97.bex_proto.DataHandler;

import de.sirati97.bex_proto.util.ByteBuffer;

public interface StreamExtractor<e> {
	public e extract(ByteBuffer dat);
}
