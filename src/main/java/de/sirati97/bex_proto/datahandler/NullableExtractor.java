package de.sirati97.bex_proto.datahandler;

import de.sirati97.bex_proto.util.CursorByteBuffer;

public class NullableExtractor<T> implements StreamExtractor<T> {
	private TypeBase base;

	public NullableExtractor(TypeBase base) {
		this.base = base;
	}

	@Override
	public T extract(CursorByteBuffer dat) {
		boolean isNull = (Boolean) Type.Boolean.getExtractor().extract(dat);
		if (isNull) {
			return null;
		} else {
			return (T) base.getExtractor().extract(dat);
		}
	}

}
