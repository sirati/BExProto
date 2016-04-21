package de.sirati97.bex_proto.datahandler;

import de.sirati97.bex_proto.util.bytebuffer.ByteBuffer;
import de.sirati97.bex_proto.v1.network.NetConnection;


public class SendStream implements Stream {
	Stream[] streams;
	ByteBuffer innerBytes;
	ByteBuffer bytes;
	IHeadlessByteArrayStream innerByteStream = new HeadlessByteArrayStreamImpl();
	
	public SendStream(Stream... streams) {
		this.streams = streams;
	}
	
	
	
	
	@Override
	public synchronized ByteBuffer getByteBuffer() {
		if (bytes == null) {
			bytes = getInnerBytes();
			bytes.seal();
			bytes = new ByteBuffer(BExStatic.setInteger(bytes.getLength()), bytes);
		}
		return bytes;
	}
	
	public synchronized ByteBuffer getInnerBytes() {
		if (innerBytes == null) {
			if (streams.length == 0) {
				innerBytes = new ByteBuffer();
			} else {
				innerBytes = streams[0].getByteBuffer();
				innerBytes.unseal();
				for (int i=1;i<streams.length;i++) {
					innerBytes.append(streams[i].getByteBuffer());
				}
			}
			streams = null;
		}
		return innerBytes;
	}


//	if (streams.length == 0)return new ByteBuffer();
//	ByteBuffer result = streams[0].getBytes();
//	result.unseal();
//	for (int i=1;i<streams.length;i++) {
//		result.append(streams[i].getBytes());
//	}
//	return result;
	
	public IHeadlessByteArrayStream getHeadlessStream() {
		return innerByteStream;
	}
	
	public void send(NetConnection connection) {
		connection.send(getByteBuffer().getBytes());
	}

	protected class HeadlessByteArrayStreamImpl implements IHeadlessByteArrayStream {
		@Override
		public ByteBuffer getByteBuffer() {
			return getInnerBytes();
		}
	}
}
