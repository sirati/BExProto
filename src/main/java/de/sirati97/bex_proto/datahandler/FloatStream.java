package de.sirati97.bex_proto.datahandler;

public class FloatStream implements Stream {
	private float data;

	public FloatStream(float data) {
		this.data = data;
	}

	@Override
	public byte[] getBytes() {
		return BExStatic.setFloat(data);
	}

}