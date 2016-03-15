package de.sirati97.bex_proto.datahandler_;




public class MultiStream implements Stream {

	Stream[] streams;
	
	public MultiStream(Stream... streams) {
		this.streams = streams;
	}
	
	
	
	
	@Override
	public byte[] getBytes() {
		byte[][] bytess = new byte[streams.length][];
		//Main.changeTrap(1);
		for (int i=0;i<streams.length;i++) {
			//System.out.println(Main.getTrap() + "Merging streams " + (i+1) + "/" + streams.length+ ": " + streams[i].toString());
			bytess[i] = streams[i].getBytes();
		}
		//Main.changeTrap(-1);
		return BExStatic.mergeStream(bytess);
	}
}
