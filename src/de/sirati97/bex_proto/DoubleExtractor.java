package de.sirati97.bex_proto;

public class DoubleExtractor implements StreamExtractor<Double> {

	@Override
	public Double extract(ExtractorDat dat) {
		return BExStatic.getDouble(dat);
	}


}
