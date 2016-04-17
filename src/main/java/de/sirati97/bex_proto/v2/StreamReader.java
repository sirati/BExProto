package de.sirati97.bex_proto.v2;

import de.sirati97.bex_proto.datahandler.BExStatic;
import de.sirati97.bex_proto.threading.AsyncHelper;
import de.sirati97.bex_proto.util.CursorByteBuffer;
import de.sirati97.bex_proto.v2.artifcon.ArtifConnection;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.security.MessageDigest;

public class StreamReader {
	private IPacket packet;
	
	public StreamReader(IPacket packet) {
		this.packet = packet;
	}
	
	public byte[] read(byte[] bytes, ArtifConnection sender, AsyncHelper asyncHelper, String name) {
		int location = 0;
		streamLoop: do {
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
					throw new IllegalStateException(e);
				}
			}
			if (sender.getHashAlgorithm() != null) {
				MessageDigest md = sender.getHashAlgorithm();
				byte[] hash = new byte[md.getDigestLength()];
				System.arraycopy(stream, 0, hash, 0, hash.length);
				byte[] newStream = new byte[stream.length-hash.length];
				System.arraycopy(stream, hash.length, newStream, 0, newStream.length);
				byte[] hash2 = md.digest(newStream);
				for (int i=0;i<hash.length;i++) {
                    if (hash[i]!=hash2[i]) {
                        continue streamLoop;
                    }
				}
				stream = newStream;
			}
			final CursorByteBuffer buf = new CursorByteBuffer(stream, sender);
			execute(buf, asyncHelper, name);
			
		} while (location < bytes.length);
		return null;
	}
	
	public void execute(final CursorByteBuffer buf, AsyncHelper asyncHelper, String name) {
		asyncHelper.runAsync(new Runnable() {
			public void run() {
				packet.extract(buf);
			}
		}, name);
	}
	
	public IPacket getPacket() {
		return packet;
	}
	
}
