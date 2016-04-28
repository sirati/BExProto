package de.sirati97.bex_proto.datahandler;


import de.sirati97.bex_proto.util.bytebuffer.ByteBuffer;

public class ArrayNullableStream implements Stream{
	private Object dataObj;
	private INullableType baseType;

	public ArrayNullableStream(INullableType baseType, Object data) {
		this.baseType = baseType;
		this.dataObj = data;
        if (data == null) {
            throw new IllegalStateException();
        }
	}
	
	
	@Override
	public ByteBuffer getByteBuffer() {
		Object[] data = (Object[]) dataObj;
		int nonNull = 0;
		boolean[] nulls = new boolean[data.length];
		for (int i=0;i<data.length;i++) {
			nulls[i] = data[i]==null;
			if (!nulls[i]) {
				nonNull+=1;
			}
		}

		ByteBuffer[] buffers = new ByteBuffer[nonNull+1];
		buffers[0] = Type.Boolean.asArray().createStream(nulls).getByteBuffer(); //contains length
		int counter = 1;
		for (int i=0;i<data.length;i++) {
			if (!nulls[i]) {
				buffers[counter++] = baseType.getInnerType().createStream(data[i]).getByteBuffer();
			}
		}
		return ByteBuffer.combine(true, buffers);
	}

}
