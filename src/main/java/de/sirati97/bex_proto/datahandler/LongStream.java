package de.sirati97.bex_proto.datahandler;

import de.sirati97.bex_proto.util.bytebuffer.ByteBuffer;

public class LongStream implements Stream {
	private long data;

	public LongStream(long data) {
		this.data = data;
	}

	@Override
	public ByteBuffer getByteBuffer() {
		return BExStatic.setLong(data);
	}

}
