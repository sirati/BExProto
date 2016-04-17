package de.sirati97.bex_proto.datahandler;

import de.sirati97.bex_proto.v1.network.NetConnection;


public class SendStream implements Stream {
	Stream[] streams;
	byte[] innerBytes;
	byte[] bytes;
	IHeadlessByteArrayStream innerByteStream = new HeadlessByteArrayStreamImpl();
	
	public SendStream(Stream... streams) {
		this.streams = streams;
	}
	
	
	
	
	@Override
	public byte[] getBytes() {
		if (bytes == null) {
			byte[] mergedBytes = getInnerBytes();
			byte[] lengthBytes = BExStatic.setInteger(mergedBytes.length);
			bytes = new byte[lengthBytes.length + mergedBytes.length];
			System.arraycopy(lengthBytes, 0, bytes, 0, lengthBytes.length);
			System.arraycopy(mergedBytes, 0, bytes, lengthBytes.length, mergedBytes.length);
		}
		return bytes;
	}
	
	public byte[] getInnerBytes() {
		if (innerBytes == null) {
			byte[][] bytess = new byte[streams.length][];
			for (int i=0;i<streams.length;i++) {
				bytess[i] = streams[i].getBytes();
			}
			innerBytes = BExStatic.mergeStream(bytess);
			innerByteStream = new HeadlessByteArrayStream(innerBytes);
		}
		return innerBytes;
	}
	
	public IHeadlessByteArrayStream getHeadlessStream() {
		return innerByteStream;
	}
	
	public void send(NetConnection connection) {
		connection.send(getBytes());
	}

	protected class HeadlessByteArrayStreamImpl implements IHeadlessByteArrayStream {
		@Override
		public byte[] getBytes() {
			return getInnerBytes();
		}
	}
}
