package de.sirati97.bex_proto;

public class ByteExtractor implements StreamExtractor<Byte> {

	@Override
	public Byte extract(ExtractorDat dat) {
		return BExStatic.getByte(dat);
	}

}
