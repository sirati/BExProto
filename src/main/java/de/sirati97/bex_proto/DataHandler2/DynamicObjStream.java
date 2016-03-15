package de.sirati97.bex_proto.DataHandler2;

public class DynamicObjStream implements Stream {
	private DynamicObj data;
	
	public DynamicObjStream(DynamicObj data) {
		this.data = data;
	}

	@Override
	public byte[] getBytes() {
		Stream type = Type.Type.createStream(data.getType());
		Stream value = data.getType().createStream(data.getValue());
		return new MultiStream(type, value).getBytes();
	}

}
