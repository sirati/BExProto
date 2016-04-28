package de.sirati97.bex_proto.datahandler;


import de.sirati97.bex_proto.util.CursorByteBuffer;

public class ArrayNullableExtractor<T> implements StreamExtractor<T[]> {
	private INullableType<T> type;

	public ArrayNullableExtractor(INullableType<T> type) {
		this.type = type;
	}
	
	@Override
	public T[] extract(CursorByteBuffer dat) {
		Boolean[] nulls = Type.Boolean.asArray().getExtractor().extract(dat);

		Object[] result = type.createArray(nulls.length);
		for (int i=0;i<nulls.length;i++) {
			if (!nulls[i]) {
				result[i] = type.getInnerType().getExtractor().extract(dat);
			}
		}
		//noinspection unchecked
		return (T[]) result;
	}

}
