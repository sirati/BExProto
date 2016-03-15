package de.sirati97.bex_proto.DataHandler;

public class NullableStream implements Stream {
	private Object data;
	private TypeBase base;
	
	public NullableStream(TypeBase base, Object data) {
		this.base = base;
		this.data = data;
	}

	@Override
	public byte[] getBytes() {
		if (data == null) {
			return Type.Boolean.createStream(true).getBytes();
		} else {
			return new MultiStream(Type.Boolean.createStream(false), base.createStream(data)).getBytes();
		}
	}

}
