package de.sirati97.bex_proto;

public class IntegerExtractor implements StreamExtractor<Integer> {


	@Override
	public Integer extract(ExtractorDat dat) {
		return BExStatic.getInteger(dat);
	}

}
