package de.sirati97.bex_proto.v1.stream;

import de.sirati97.bex_proto.datahandler.Type;
import de.sirati97.bex_proto.util.bytebuffer.ByteBuffer;
import de.sirati97.bex_proto.v1.network.NetConnection;


public class SendStream implements Stream {
	Stream[] Streams;
	ByteBuffer innerBytes;
	ByteBuffer bytes;
	IHeadlessByteArrayStream innerByteStream = new HeadlessByteArrayStreamImpl();
	
	public SendStream(Stream... Streams) {
		this.Streams = Streams;
	}
	
	
	
	
	@Override
	public synchronized ByteBuffer getByteBuffer() {
		if (bytes == null) {
			bytes = getInnerBytes();
			bytes.seal();
			bytes = new ByteBuffer(Type.Integer.getEncoder().encodeIndependent(bytes.getLength()), bytes);
		}
		return bytes;
	}
	
	public synchronized ByteBuffer getInnerBytes() {
		if (innerBytes == null) {
			if (Streams.length == 0) {
				innerBytes = new ByteBuffer();
			} else {
				innerBytes = Streams[0].getByteBuffer();
				innerBytes.unseal();
				for (int i = 1; i< Streams.length; i++) {
					innerBytes.append(Streams[i].getByteBuffer());
				}
			}
			Streams = null;
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
