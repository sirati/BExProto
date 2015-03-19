package de.sirati97.bex_proto;

public class ByteStream implements Stream {
	private byte data;

	public ByteStream(byte data) {
		this.data = data;
	}

	@Override
	public byte[] getBytes() {
		return BExStatic.setByte(data);
	}

}
