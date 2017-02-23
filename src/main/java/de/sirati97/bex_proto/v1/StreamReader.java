package de.sirati97.bex_proto.v1;

import de.sirati97.bex_proto.datahandler.BExStatic;
import de.sirati97.bex_proto.util.CursorByteBuffer;
import de.sirati97.bex_proto.datahandler.VoidDecoder;
import de.sirati97.bex_proto.threading.IAsyncHelper;
import de.sirati97.bex_proto.v1.network.NetConnection;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

public class StreamReader {
	private VoidDecoder extractor;
	
	public StreamReader(VoidDecoder extractor) {
		this.extractor = extractor;
	}
	
	public byte[] read(byte[] bytes, NetConnection sender, IAsyncHelper asyncHelper, String name) {
		int location = 0;
		do {
			int streamLength = BExStatic.getInteger(bytes, location);
			location +=4;
			byte[] stream = new byte[streamLength];
			if (location +streamLength > bytes.length) {
				location -=4;
				byte[] overflow = new byte[bytes.length-location];
				System.arraycopy(bytes, location, overflow, 0, bytes.length-location);
//				System.out.println("Overflow: "+Main.bytesToString(overflow));
				return overflow;
			}
			
			System.arraycopy(bytes, location, stream, 0, streamLength);
			location +=streamLength;
			if (sender.getReceiveCipher() != null) {
				try {
					stream = sender.getReceiveCipher().doFinal(stream);
				} catch (IllegalBlockSizeException | BadPaddingException e) {
					System.err.println(sender.getPort());
					throw new IllegalStateException(e);
				}
			}
//			System.out.println("Stream: " + Main.bytesToString(stream));
			final CursorByteBuffer dat = new CursorByteBuffer(stream, sender);
			exercute(dat, sender, asyncHelper, name);
			
		} while (location < bytes.length);
		return null;
	}
	
	public void exercute(final CursorByteBuffer dat, NetConnection sender, IAsyncHelper asyncHelper, String name) {
		asyncHelper.runAsync(new Runnable() {
			public void run() {
				extractor.decode(dat);
			}
		}, name);
	}
	
	public VoidDecoder getExtractor() {
		return extractor;
	}
	
}
