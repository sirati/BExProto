package de.sirati97.bex_proto;

import de.sirati97.bex_proto.network.NetConnection;


public class SendStream implements Stream {
	Stream[] streams;
	
	public SendStream(Stream... streams) {
		this.streams = streams;
	}
	
	
	
	
	@Override
	public byte[] getBytes() {
		byte[][] bytess = new byte[streams.length][];
		for (int i=0;i<streams.length;i++) {
			bytess[i] = streams[i].getBytes();
		}
		byte[] mergedBytes = BExStatic.mergeStream(bytess);
		byte[] lenghtBytes = BExStatic.setInteger(mergedBytes.length);
		byte[] result = new byte[lenghtBytes.length + mergedBytes.length];
		System.arraycopy(lenghtBytes, 0, result, 0, lenghtBytes.length);
		System.arraycopy(mergedBytes, 0, result, lenghtBytes.length, mergedBytes.length);
		return result;
	}
	
	public void send(NetConnection connection) {
		connection.send(getBytes());
	}


}
