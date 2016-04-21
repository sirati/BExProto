package de.sirati97.bex_proto.datahandler;

import de.sirati97.bex_proto.util.bytebuffer.ByteBuffer;

public class DynamicObjStream implements Stream {
	private DynamicObj data;
	
	public DynamicObjStream(DynamicObj data) {
		this.data = data;
	}

	@Override
	public ByteBuffer getByteBuffer() {
		Stream type = Type.Type.createStream(data.getType());
		Stream value = data.getType().createStream(data.getValue());
		return new MultiStream(type, value).getByteBuffer();
	}

}
