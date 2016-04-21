package de.sirati97.bex_proto.datahandler;

import de.sirati97.bex_proto.util.bytebuffer.ByteBuffer;

import java.nio.charset.Charset;

public class StringStream implements Stream {
	private String str;
	private Charset charset;
	
	public StringStream(String str, Charset charset) {
		this.str = str;
		this.charset = charset;
		
		
	}

	@Override
	public ByteBuffer getByteBuffer() {
		return BExStatic.setString(str, charset);
	}

}
