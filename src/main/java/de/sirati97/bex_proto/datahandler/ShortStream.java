package de.sirati97.bex_proto.datahandler;

import de.sirati97.bex_proto.util.bytebuffer.ByteBuffer;

public class ShortStream implements Stream {
	private short data;

	public ShortStream(short data) {
		this.data = data;
	}

	@Override
	public ByteBuffer getBytes() {
		return BExStatic.setShort(data);
	}

}
