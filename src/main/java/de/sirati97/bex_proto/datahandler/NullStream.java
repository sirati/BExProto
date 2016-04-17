package de.sirati97.bex_proto.datahandler;

import de.sirati97.bex_proto.util.bytebuffer.ByteBuffer;

public class NullStream implements Stream {

	@Override
	public ByteBuffer getBytes() {
		return new ByteBuffer();
	}

}
