package de.sirati97.bex_proto.datahandler_;

import java.nio.charset.Charset;

public class StringType extends ObjType {
	private Charset charset;
	private StreamExtractor<? extends Object> extractor
;
	
	public StringType(Charset charset) {
		this.charset = charset;
		this.extractor = new StringExtractor(charset);
		register();
	}

	@Override
	public Stream createStream(Object obj) {
		return new StringStream((String)obj, charset);
	}

	@Override
	public StreamExtractor<? extends Object> getExtractor() {
		return extractor;
	}

	@Override
	public Object[] createArray(int lenght) {
		return new String[lenght];
	}

	@Override
	public Class<?> getType() {
		return String.class;
	}

	@Override
	public String getTypeName() {
		return "String_" + charset.name();
	}

	
	@Override
	protected boolean earlyRegister() {
		return false;
	}
}
