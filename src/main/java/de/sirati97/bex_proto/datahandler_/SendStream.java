package de.sirati97.bex_proto.datahandler_;

import de.sirati97.bex_proto.v1.network.NetConnection;


public class SendStream implements Stream {
	Stream[] streams;
	byte[] innerBytes;
	HeadlessByteStream innerByteStream;
	
	public SendStream(Stream... streams) {
		this.streams = streams;
	}
	
	
	
	
	@Override
	public byte[] getBytes() {
		byte[] mergedBytes;
		if (innerBytes == null) {
			byte[][] bytess = new byte[streams.length][];
			for (int i=0;i<streams.length;i++) {
				bytess[i] = streams[i].getBytes();
			}
			mergedBytes = BExStatic.mergeStream(bytess);
			innerBytes = mergedBytes;
			innerByteStream = new HeadlessByteStream(innerBytes);
		} else {
			mergedBytes = innerBytes;
		}
		
		
		byte[] lenghtBytes = BExStatic.setInteger(mergedBytes.length);
		byte[] result = new byte[lenghtBytes.length + mergedBytes.length];
		System.arraycopy(lenghtBytes, 0, result, 0, lenghtBytes.length);
		System.arraycopy(mergedBytes, 0, result, lenghtBytes.length, mergedBytes.length);
		return result;
	}
	
	public byte[] getInnerBytes() {
		return innerBytes;
	}
	
	public HeadlessByteStream getInnerByteStream() {
		return innerByteStream;
	}
	
	public void send(NetConnection connection) {
		connection.send(getBytes());
	}


}
