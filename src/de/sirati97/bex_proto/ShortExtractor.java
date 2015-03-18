package de.sirati97.bex_proto;

public class ShortExtractor implements StreamExtractor<Short> {

	@Override
	public Short extract(ExtractorDat dat) {
		return BExStatic.getShort(dat);
	}

}
