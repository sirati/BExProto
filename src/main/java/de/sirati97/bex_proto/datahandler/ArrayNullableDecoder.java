package de.sirati97.bex_proto.datahandler;


import de.sirati97.bex_proto.util.CursorByteBuffer;

public class ArrayNullableDecoder<T> extends DecoderBase<T[]> {
	private INullableType<T> type;

	public ArrayNullableDecoder(INullableType<T> type) {
		this.type = type;
	}
	
	@Override
	public T[] decode(CursorByteBuffer dat, boolean header) {
		Boolean[] nulls = Type.Boolean.asArray().getDecoder().decode(dat);

		Object[] result = type.createArray(nulls.length);
		for (int i=0;i<nulls.length;i++) {
			if (!nulls[i]) {
				result[i] = type.getInnerType().getDecoder().decode(dat);
			}
		}
		//noinspection unchecked
		return (T[]) result;
	}

}
