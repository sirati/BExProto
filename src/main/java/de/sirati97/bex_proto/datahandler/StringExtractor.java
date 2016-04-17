package de.sirati97.bex_proto.datahandler;

import de.sirati97.bex_proto.util.CursorByteBuffer;

import java.nio.charset.Charset;

public class StringExtractor implements StreamExtractor<String> {
	private Charset charset;
	
	public StringExtractor(Charset charset) {
		this.charset = charset;
	}

	@Override
	public String extract(CursorByteBuffer dat) {
		return BExStatic.getString(dat, charset);
	}

}
