package de.sirati97.bex_proto.datahandler;

import de.sirati97.bex_proto.util.bytebuffer.ByteBuffer;

public class BooleanStream implements Stream {
	private boolean data;

	public BooleanStream(boolean data) {
		this.data = data;
	}

	@Override
	public ByteBuffer getBytes() {
		return BExStatic.setBoolean(data);
	}

}
