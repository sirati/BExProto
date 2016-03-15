package de.sirati97.bex_proto.datahandler_;

import de.sirati97.bex_proto.util.ByteBuffer;

import java.nio.charset.Charset;

public class StringExtractor implements StreamExtractor<String> {
	private Charset charset;
	
	public StringExtractor(Charset charset) {
		this.charset = charset;
	}

	@Override
	public String extract(ByteBuffer dat) {
		return BExStatic.getString(dat, charset);
	}

}
