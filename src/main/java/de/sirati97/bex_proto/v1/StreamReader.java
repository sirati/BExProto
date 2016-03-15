package de.sirati97.bex_proto.v1;

import de.sirati97.bex_proto.DataHandler2.BExStatic;
import de.sirati97.bex_proto.util.ByteBuffer;
import de.sirati97.bex_proto.DataHandler2.VoidExtractor;
import de.sirati97.bex_proto.threading.AsyncHelper;
import de.sirati97.bex_proto.v1.network.NetConnection;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

public class StreamReader {
	private VoidExtractor extractor;
	
	public StreamReader(VoidExtractor extractor) {
		this.extractor = extractor;
	}
	
	public byte[] read(byte[] bytes, NetConnection sender, AsyncHelper asyncHelper, String name) {
		int location = 0;
		do {
			int streamLenght = BExStatic.getInteger(bytes, location);
			location +=4;
			byte[] stream = new byte[streamLenght];
			if (location +streamLenght > bytes.length) {
				location -=4;
				byte[] overflow = new byte[bytes.length-location];
				System.arraycopy(bytes, location, overflow, 0, bytes.length-location);
//				System.out.println("Overflow: "+Main.bytesToString(overflow));
				return overflow;
			}
			
			System.arraycopy(bytes, location, stream, 0, streamLenght);
			location +=streamLenght;
			if (sender.getReceiveCipher() != null) {
				try {
					stream = sender.getReceiveCipher().doFinal(stream);
				} catch (IllegalBlockSizeException | BadPaddingException e) {
					System.err.println(sender.getPort());
					throw new IllegalStateException(e);
				}
			}
//			System.out.println("Stream: " + Main.bytesToString(stream));
			final ByteBuffer dat = new ByteBuffer(stream, sender);
			exercute(dat, sender, asyncHelper, name);
			
		} while (location < bytes.length);
		return null;
	}
	
	public void exercute(final ByteBuffer dat, NetConnection sender, AsyncHelper asyncHelper, String name) {
		asyncHelper.runAsync(new Runnable() {
			public void run() {
				extractor.extract(dat);
			}
		}, name);
	}
	
	public VoidExtractor getExtractor() {
		return extractor;
	}
	
}
