package de.sirati97.bex_proto;

import java.nio.charset.Charset;

public class StringExtractor implements StreamExtractor<String> {
	private Charset charset;
	
	public StringExtractor(Charset charset) {
		this.charset = charset;
	}

	@Override
	public String extract(ExtractorDat dat) {
		return BExStatic.getString(dat, charset);
	}

}
