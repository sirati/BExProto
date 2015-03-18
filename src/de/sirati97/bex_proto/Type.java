package de.sirati97.bex_proto;

import com.google.common.base.Charsets;

public abstract class Type implements TypeBase{
	public static final Type String_Utf_8 = new StringType(Charsets.UTF_8);
	public static final Type String_Utf_16 = new StringType(Charsets.UTF_16);
	public static final Type String_ISO_8859_1 = new StringType(Charsets.ISO_8859_1);
	public static final Type String_US_ASCII = new StringType(Charsets.US_ASCII);
	public static final Type Integer = new Type() {
		StreamExtractor<? extends Object> extractor = new IntegerExtractor();
		
		@Override public Stream createStream(Object obj) {
			return new IntegerStream((int)obj);
		}

		@Override public StreamExtractor<? extends Object> getExtractor() {
			return extractor;
		}

		@Override public Object[] createArray(int lenght) {
			return new Integer[lenght];
		}
	};
	public static final Type Long = new Type() {
		StreamExtractor<? extends Object> extractor = new LongExtractor();
		
		@Override public Stream createStream(Object obj) {
			return new LongStream((long)obj);
		}

		@Override public StreamExtractor<? extends Object> getExtractor() {
			return extractor;
		}

		@Override public Object[] createArray(int lenght) {
			return new Long[lenght];
		}
	};
	public static final Type Short = new Type() {
		StreamExtractor<? extends Object> extractor = new ShortExtractor();
		
		@Override public Stream createStream(Object obj) {
			return new ShortStream((short)obj);
		}

		@Override public StreamExtractor<? extends Object> getExtractor() {
			return extractor;
		}

		@Override public Object[] createArray(int lenght) {
			return new Short[lenght];
		}
	};
	public static final Type Byte = new Type() {
		StreamExtractor<? extends Object> extractor = new ByteExtractor();
		
		@Override public Stream createStream(Object obj) {
			return new ByteStream((byte)obj);
		}

		@Override public StreamExtractor<? extends Object> getExtractor() {
			return extractor;
		}

		@Override public Object[] createArray(int lenght) {
			return new Byte[lenght];
		}
	};
	public static final Type Double = new Type() {
		StreamExtractor<? extends Object> extractor = new DoubleExtractor();
		
		@Override public Stream createStream(Object obj) {
			return new DoubleStream((double)obj);
		}

		@Override public StreamExtractor<? extends Object> getExtractor() {
			return extractor;
		}

		@Override public Object[] createArray(int lenght) {
			return new Double[lenght];
		}
	};
	
	
	@Override public boolean isPremitive() {return true;}
	@Override public boolean isArray() {return false;}
}
