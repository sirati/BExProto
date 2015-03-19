package de.sirati97.bex_proto;

public class FloatExtractor implements StreamExtractor<Float> {

	@Override
	public Float extract(ExtractorDat dat) {
		return BExStatic.getFloat(dat);
	}


}
