package de.sirati97.bex_proto.DataHandler;

public class IntegerStream implements Stream {
	private int data;

	public IntegerStream(int data) {
		this.data = data;
	}

	@Override
	public byte[] getBytes() {
		return BExStatic.setInteger(data);
	}

}
