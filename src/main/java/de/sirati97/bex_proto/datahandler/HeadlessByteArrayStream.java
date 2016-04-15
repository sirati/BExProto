package de.sirati97.bex_proto.datahandler;

public class HeadlessByteArrayStream implements IHeadlessByteArrayStream {
	private byte[] stream;
	
	public HeadlessByteArrayStream(byte[] stream) {
		this.stream = stream;
	}
	
	@Override
	public byte[] getBytes() {
		return stream;
	}

}
