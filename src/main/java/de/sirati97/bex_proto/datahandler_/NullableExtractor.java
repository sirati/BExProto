package de.sirati97.bex_proto.datahandler_;

import de.sirati97.bex_proto.util.ByteBuffer;

public class NullableExtractor implements StreamExtractor<Object> {
	private TypeBase base;

	public NullableExtractor(TypeBase base) {
		this.base = base;
	}

	@Override
	public Object extract(ByteBuffer dat) {
		boolean nulled = (Boolean) Type.Boolean.getExtractor().extract(dat);
		if (nulled) {
			return null;
		} else {
			return base.getExtractor().extract(dat);
		}
	}

}
