package de.sirati97.bex_proto.datahandler;

public class ShortStream implements Stream {
	private short data;

	public ShortStream(short data) {
		this.data = data;
	}

	@Override
	public byte[] getBytes() {
		return BExStatic.setShort(data);
	}

}
