package de.sirati97.bex_proto.datahandler;

import de.sirati97.bex_proto.util.bytebuffer.ByteBuffer;

public class IntegerStream implements Stream {
	private int data;

	public IntegerStream(int data) {
		this.data = data;
	}

	@Override
	public ByteBuffer getByteBuffer() {
		return BExStatic.setInteger(data);
	}

}
