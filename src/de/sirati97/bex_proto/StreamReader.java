package de.sirati97.bex_proto;

public class StreamReader {
	StreamExtractor<?>[] extractors;
	
	public StreamReader(StreamExtractor<?>... extractors) {
		this.extractors = extractors;
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
			Object[] extractedData = new Object[extractors.length];
			for (int i=0;i<extractors.length;i++) {
				extractedData[i] = extractors[i].extract(dat);
			}
			run(extractedData);
		} while (location < bytes.length);
		
	}
	
	public void run(Object... data) {
		
	}
	
	
}
