package de.sirati97.bex_proto.datahandler;


import de.sirati97.bex_proto.util.bytebuffer.ByteBuffer;

public class ArrayEncoder<T> extends EncoderBase<T[]> {
	private IType<T> baseType;
	
	public ArrayEncoder(IType<T> baseType) {
		this.baseType = baseType;
	}

//    public ByteBuffer getByteBufferObj(Object dataObj) {
//        if (baseType instanceof PrimitiveType) {
//            dataObj = ((PrimitiveType) baseType).toObjectArray(dataObj);
//        }
//        return getByteBuffer((T[])dataObj);
//    }
	
	@Override
	public void encode(T[] data, ByteBuffer buffer) {
		BExStatic.setInteger(data.length, buffer);
        for (T aData : data) {
            baseType.getEncoder().encode(aData, buffer);
        }
	}

    @Override
    public void encodeObj(Object data, ByteBuffer buffer) {
        if (baseType instanceof PrimitiveType) {
            data = ((PrimitiveType) baseType).toObjectArray(data);
        }
        super.encodeObj(data, buffer);
    }
}
