package de.sirati97.bex_proto;

public class BooleanExtractor implements StreamExtractor<Boolean> {

	@Override
	public Boolean extract(ExtractorDat dat) {
		return BExStatic.getBoolean(dat);
	}


}
