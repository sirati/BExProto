package de.sirati97.bex_proto.datahandler;

import de.sirati97.bex_proto.util.CursorByteBuffer;

import java.nio.charset.Charset;

public class StringDecoder implements IDecoder<String> {
	private Charset charset;
	
	public StringDecoder(Charset charset) {
		this.charset = charset;
	}

	@Override
	public String decode(CursorByteBuffer dat) {
		return BExStatic.getString(dat, charset);
	}

}
