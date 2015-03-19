package de.sirati97.bex_proto;

import de.sirati97.bex_proto.debug.Main;

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
			System.out.println("MultiStream["+i+"] "+Main.bytesToString(streams[i].getBytes()));
		}
		System.out.println("MultiStream "+Main.bytesToString(BExStatic.mergeStream(bytess)));
		return BExStatic.mergeStream(bytess);
	}
}
