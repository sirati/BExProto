package de.sirati97.bex_proto;

public class NullableExtractor implements StreamExtractor<Object> {
	private TypeBase base;

	public NullableExtractor(TypeBase base) {
		this.base = base;
	}

	@Override
	public Object extract(ExtractorDat dat) {
		boolean nulled = (Boolean) Type.Boolean.getExtractor().extract(dat);
		if (nulled) {
			return null;
		} else {
			return base.getExtractor().extract(dat);
		}
	}

}
