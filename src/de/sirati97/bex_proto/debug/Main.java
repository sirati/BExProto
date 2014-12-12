package de.sirati97.bex_proto.debug;

import com.google.common.base.Charsets;

import de.sirati97.bex_proto.SendStream;
import de.sirati97.bex_proto.StreamExtractor;
import de.sirati97.bex_proto.StreamReader;
import de.sirati97.bex_proto.StringExtractor;
import de.sirati97.bex_proto.StringStream;

public class Main {

	public static void main(String[] args) {
		System.out.println("Test 1");
		byte[] stream2_1 = new SendStream(new StringStream("Erfolgreich1", Charsets.ISO_8859_1),new StringStream("Erfolgreich 2", Charsets.ISO_8859_1)).getBytes();
		byte[] stream2_2 = new SendStream(new StringStream("Erfolgreich  3", Charsets.ISO_8859_1),new StringStream("Erfolgreich   4", Charsets.ISO_8859_1)).getBytes();
		byte[] stream = new byte[stream2_1.length + stream2_2.length];
		System.arraycopy(stream2_1, 0, stream, 0, stream2_1.length);
		System.arraycopy(stream2_2, 0, stream, stream2_1.length,
				stream2_2.length);
		
		Reader r = new Main.Reader(new StringExtractor(Charsets.ISO_8859_1),new StringExtractor(Charsets.ISO_8859_1));
		r.read(stream);

	}

	
	
	static class Reader extends StreamReader {
		public Reader(StreamExtractor<?>... extractors) {
			super(extractors);
		}
		
		@Override
		public void run(Object... data) {
			for (Object obj:data) {
				System.out.println(obj.toString());
			}
		}
	}
}
