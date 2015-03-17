package de.sirati97.bex_proto;

import java.nio.charset.Charset;

public class StringStream implements Stream {
	private String str;
	private Charset charset;
	
	public StringStream(String str, Charset charset) {
		this.str = str;
		this.charset = charset;
		
		
	}

	@Override
	public byte[] getBytes() {
		return BExStatic.setString(str, charset);
	}

}
