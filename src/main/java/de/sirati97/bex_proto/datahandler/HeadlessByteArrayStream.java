package de.sirati97.bex_proto.datahandler;

import de.sirati97.bex_proto.util.bytebuffer.ByteBuffer;

public class HeadlessByteArrayStream implements IHeadlessByteArrayStream {
	private ByteBuffer stream;
	
	public HeadlessByteArrayStream(ByteBuffer stream) {
		this.stream = stream;
	}
	
	@Override
	public ByteBuffer getBytes() {
		return stream;
	}

}
