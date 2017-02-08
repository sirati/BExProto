package de.sirati97.bex_proto.datahandler;

import de.sirati97.bex_proto.util.CursorByteBuffer;

import java.nio.charset.Charset;

public class StringDecoder extends DecoderBase<String> {
	private Charset charset;
	
	public StringDecoder(Charset charset) {
		this.charset = charset;
	}

	@Override
	public String decode(CursorByteBuffer dat, boolean header) {
		return BExStatic.getString(dat, charset, header);
	}

}
