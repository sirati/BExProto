package de.sirati97.bex_proto.datahandler;

import java.nio.charset.Charset;

public class StringType extends ObjType<String> {
	private Charset charset;
	private StreamExtractor<String> extractor
;
	
	public StringType(Charset charset) {
		this.charset = charset;
		this.extractor = new StringExtractor(charset);
		register();
	}

	@Override
	public Stream createStreamCasted(String obj) {
		return new StringStream(obj, charset);
	}

	@Override
	public StreamExtractor<String> getExtractor() {
		return extractor;
	}

	@Override
	public Class<String> getType() {
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
