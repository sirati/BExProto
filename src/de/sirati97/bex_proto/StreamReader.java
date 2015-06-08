package de.sirati97.bex_proto;

import de.sirati97.bex_proto.network.AsyncHelper;
import de.sirati97.bex_proto.network.NetConnection;

public class StreamReader {
	VoidExtractor extractor;
	
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
				return overflow;
			}
			
			System.arraycopy(bytes, location, stream, 0, streamLenght);
			location +=streamLenght;
			final ExtractorDat dat = new ExtractorDat(stream, sender);
			asyncHelper.runAsync(new Runnable() {
				public void run() {
					extractor.extract(dat);
				}
			}, name);
		} while (location < bytes.length);
		return null;
	}
	
	public VoidExtractor getExtractor() {
		return extractor;
	}
	
}
