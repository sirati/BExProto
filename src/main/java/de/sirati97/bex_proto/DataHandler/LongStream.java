package de.sirati97.bex_proto.datahandler;

public class LongStream implements Stream {
	private long data;

	public LongStream(long data) {
		this.data = data;
	}

	@Override
	public byte[] getBytes() {
		return BExStatic.setLong(data);
	}

}