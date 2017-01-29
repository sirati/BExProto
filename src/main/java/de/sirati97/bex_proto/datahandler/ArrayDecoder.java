package de.sirati97.bex_proto.datahandler;


import de.sirati97.bex_proto.util.CursorByteBuffer;

public class ArrayDecoder<T> implements IDecoder<T[]> {
	private IType type;
	
	public ArrayDecoder(IType type) {
		this.type = type;
	}
	
	@Override
	public T[] decode(CursorByteBuffer dat) {
		int length = (Integer) Type.Integer.getDecoder().decode(dat);
		Object[] result = type.createArray(length);
		for (int i=0;i<length;i++) {
			result[i] = type.getDecoder().decode(dat);
		}
		return (T[]) result;
	}

}
