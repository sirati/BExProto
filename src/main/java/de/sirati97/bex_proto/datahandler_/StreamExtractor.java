package de.sirati97.bex_proto.datahandler_;

import de.sirati97.bex_proto.util.ByteBuffer;

public interface StreamExtractor<e> {
	public e extract(ByteBuffer dat);
}
