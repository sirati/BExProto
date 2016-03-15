package de.sirati97.bex_proto.datahandler;

public class HeadlessByteStream implements IHeadlessByteStream {
	private byte[] stream;
	
	public HeadlessByteStream(byte[] stream) {
		this.stream = stream;
	}
	
	@Override
	public byte[] getBytes() {
		return stream;
	}

}
