package de.sirati97.bex_proto.datahandler;


import de.sirati97.bex_proto.util.bytebuffer.ByteBuffer;

public class ArrayStream implements Stream{
	private Object dataObj;
	private TypeBase baseType;
	
	public ArrayStream(TypeBase baseType, Object data) {
		this.baseType = baseType;
		if (baseType instanceof PrimitiveType) {
			data = ((PrimitiveType) baseType).toObjectArray(data);
		}
		this.dataObj = data;
	}
	
	
	@Override
	public ByteBuffer getByteBuffer() {
		Object[] data = (Object[]) dataObj;
		
		ByteBuffer[] buffers = new ByteBuffer[data.length+1];
		buffers[0] = BExStatic.setInteger(data.length);
		for (int i=0;i<data.length;i++) {
			buffers[i+1] = baseType.createStream(data[i]).getByteBuffer();
		}
		return ByteBuffer.combine(true, buffers);
	}

}
