package de.sirati97.bex_proto.datahandler;


import de.sirati97.bex_proto.util.CursorByteBuffer;

public class ArrayExtractor<T> implements StreamExtractor<T[]> {
	private TypeBase type;
	
	public ArrayExtractor(TypeBase type) {
		this.type = type;
	}
	
	@Override
	public T[] extract(CursorByteBuffer dat) {
		int length = (Integer) Type.Integer.getExtractor().extract(dat);
		Object[] result = type.createArray(length);
		for (int i=0;i<length;i++) {
			result[i] = type.getExtractor().extract(dat);
		}
		return (T[]) result;
	}

}
