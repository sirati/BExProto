package de.sirati97.bex_proto;

public class LongExtractor implements StreamExtractor<Long> {

	@Override
	public Long extract(ExtractorDat dat) {
		return BExStatic.getLong(dat);
	}

}
