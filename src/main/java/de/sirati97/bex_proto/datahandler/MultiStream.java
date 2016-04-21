package de.sirati97.bex_proto.datahandler;


import de.sirati97.bex_proto.util.bytebuffer.ByteBuffer;

public class MultiStream implements Stream {

	Stream[] streams;
	
	public MultiStream(Stream... streams) {
		this.streams = streams;
	}
	
	
	
	
	@Override
	public ByteBuffer getByteBuffer() {
		if (streams.length == 0)return new ByteBuffer();
		ByteBuffer result = streams[0].getByteBuffer();
		result.unseal();
		for (int i=1;i<streams.length;i++) {
			result.append(streams[i].getByteBuffer());
		}
		return result;
	}
}
