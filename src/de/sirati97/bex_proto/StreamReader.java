package de.sirati97.bex_proto;

public class StreamReader {
	VoidExtractor extractor;
	
	public StreamReader(VoidExtractor extractor) {
		this.extractor = extractor;
	}
	
	public void read(byte[] bytes) {
		int location = 0;
		do {
			int streamLenght = BExStatic.getInteger(bytes, location);
			location +=4;
			byte[] stream = new byte[streamLenght];
			System.arraycopy(bytes, location, stream, 0, streamLenght);
			location +=streamLenght;
			ExtractorDat dat = new ExtractorDat(stream);
			extractor.extract(dat);
		} while (location < bytes.length);
		
	}
	
	
	
}
