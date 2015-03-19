package de.sirati97.bex_proto;


public class MultiStream implements Stream {

	Stream[] streams;
	
	public MultiStream(Stream... streams) {
		this.streams = streams;
	}
	
	
	
	
	@Override
	public byte[] getBytes() {
		byte[][] bytess = new byte[streams.length][];
		for (int i=0;i<streams.length;i++) {
			bytess[i] = streams[i].getBytes();
		}
		return BExStatic.mergeStream(bytess);
	}
}
