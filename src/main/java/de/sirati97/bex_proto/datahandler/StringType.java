package de.sirati97.bex_proto.datahandler;

import java.nio.charset.Charset;

public class StringType extends ObjType<String> {
	private Charset charset;
;
	
	public StringType(Charset charset) {
	    super(new StringEncoder(charset), new StringDecoder(charset));
		this.charset = charset;
		register();
	}

    @Override
    public boolean isEncodable(Object obj, boolean platformIndependent) {
        return obj instanceof String;
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
