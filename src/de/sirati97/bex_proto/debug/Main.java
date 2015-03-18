package de.sirati97.bex_proto.debug;

import de.sirati97.bex_proto.SendStream;
import de.sirati97.bex_proto.StreamExtractor;
import de.sirati97.bex_proto.StreamReader;

public class Main {

	public static void main(String[] args) {
		
		
		TestCommand command = new TestCommand();
		byte[] stream = new SendStream(command.send("ABCabcÄÖÜäöü^°123óò", "ABCabcÄÖÜäöü^°123óò", "ABCabcÄÖÜäöü^°123óò", "ABCabcÄÖÜäöü^°123óò", 1L, 2, (short)3, (byte)4, 3.5, new int[]{9,8,7})).getBytes();
		StreamReader reader = new StreamReader(command);
		reader.read(stream);
		
//		System.out.println("Test 1");
//		byte[] stream2_1 = new SendStream(new StringStream("Erfolgreich1", Charsets.ISO_8859_1),new StringStream("Erfolgreich 2", Charsets.ISO_8859_1)).getBytes();
//		byte[] stream2_2 = new SendStream(new StringStream("Erfolgreich  3", Charsets.ISO_8859_1),new StringStream("Erfolgreich   4", Charsets.ISO_8859_1),new StringStream("Erfolgreich    5", Charsets.ISO_8859_1)).getBytes();
//		byte[] stream = new byte[stream2_1.length + stream2_2.length];
//		System.arraycopy(stream2_1, 0, stream, 0, stream2_1.length);
//		System.arraycopy(stream2_2, 0, stream, stream2_1.length,
//				stream2_2.length);
//		
//		Reader r = new Main.Reader(new StringExtractor(Charsets.ISO_8859_1),new StringExtractor(Charsets.ISO_8859_1));
//		r.read(stream);

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
