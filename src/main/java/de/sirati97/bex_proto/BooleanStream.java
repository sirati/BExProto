package de.sirati97.bex_proto;

public class BooleanStream implements Stream {
	private boolean data;

	public BooleanStream(boolean data) {
		this.data = data;
	}

	@Override
	public byte[] getBytes() {
		return BExStatic.setBoolean(data);
	}

}
