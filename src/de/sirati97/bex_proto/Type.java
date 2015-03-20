package de.sirati97.bex_proto;

import org.apache.commons.lang3.ArrayUtils;

import com.google.common.base.Charsets;

import de.sirati97.bex_proto.network.adv.SSCWrapperType;

public abstract class Type implements TypeBase{
	public static final Type SSCWrapper = new SSCWrapperType();
	public static final Type String_Utf_8 = new StringType(Charsets.UTF_8);
	public static final Type String_Utf_16 = new StringType(Charsets.UTF_16);
	public static final Type String_ISO_8859_1 = new StringType(Charsets.ISO_8859_1);
	public static final Type String_US_ASCII = new StringType(Charsets.US_ASCII);
	public static final PremitivType Integer = new PremitivType() {
		IntegerExtractor extractor = new IntegerExtractor();
		
		@Override public Stream createStream(Object obj) {
			return new IntegerStream((int)obj);
		}

		@Override public IntegerExtractor getExtractor() {
			return extractor;
		}

		@Override public Object[] createArray(int lenght) {
			return new Integer[lenght];
		}

		@Override public Object toPremitiveArray(Object obj) {
			return ArrayUtils.toPrimitive((Integer[])obj);
		}

		@Override public Object toObjectArray(Object obj) {
			return ArrayUtils.toObject((int[])obj);
		}

		@Override public Class<?> getType() {
			return int.class;
		}
	};
	public static final PremitivType Long = new PremitivType() {
		LongExtractor extractor = new LongExtractor();
		
		@Override public Stream createStream(Object obj) {
			return new LongStream((long)obj);
		}

		@Override public LongExtractor getExtractor() {
			return extractor;
		}

		@Override public Object[] createArray(int lenght) {
			return new Long[lenght];
		}

		@Override public Object toPremitiveArray(Object obj) {
			return ArrayUtils.toPrimitive((Long[])obj);
		}

		@Override public Object toObjectArray(Object obj) {
			return ArrayUtils.toObject((long[])obj);
		}

		@Override public Class<?> getType() {
			return long.class;
		}
	};
	public static final PremitivType Short = new PremitivType() {
		ShortExtractor extractor = new ShortExtractor();
		
		@Override public Stream createStream(Object obj) {
			return new ShortStream((short)obj);
		}

		@Override public ShortExtractor getExtractor() {
			return extractor;
		}

		@Override public Object[] createArray(int lenght) {
			return new Short[lenght];
		}

		@Override public Object toPremitiveArray(Object obj) {
			return ArrayUtils.toPrimitive((Short[])obj);
		}

		@Override public Object toObjectArray(Object obj) {
			return ArrayUtils.toObject((short[])obj);
		}

		@Override public Class<?> getType() {
			return short.class;
		}
	};
	public static final PremitivType Byte = new PremitivType() {
		ByteExtractor extractor = new ByteExtractor();
		
		@Override public Stream createStream(Object obj) {
			return new ByteStream((byte)obj);
		}

		@Override public ByteExtractor getExtractor() {
			return extractor;
		}

		@Override public Object[] createArray(int lenght) {
			return new Byte[lenght];
		}

		@Override public Object toPremitiveArray(Object obj) {
			return ArrayUtils.toPrimitive((Byte[])obj);
		}

		@Override public Object toObjectArray(Object obj) {
			return ArrayUtils.toObject((byte[])obj);
		}

		@Override public Class<?> getType() {
			return byte.class;
		}
	};
	public static final PremitivType Double = new PremitivType() {
		DoubleExtractor extractor = new DoubleExtractor();
		
		@Override public Stream createStream(Object obj) {
			return new DoubleStream((double)obj);
		}

		@Override public DoubleExtractor getExtractor() {
			return extractor;
		}

		@Override public Object[] createArray(int lenght) {
			return new Double[lenght];
		}

		@Override public Object toPremitiveArray(Object obj) {
			return ArrayUtils.toPrimitive((Double[])obj);
		}

		@Override public Object toObjectArray(Object obj) {
			return ArrayUtils.toObject((double[])obj);
		}

		@Override public Class<?> getType() {
			return double.class;
		}
	};
	public static final PremitivType Float = new PremitivType() {
		FloatExtractor extractor = new FloatExtractor();
		
		@Override public Stream createStream(Object obj) {
			return new FloatStream((float)obj);
		}

		@Override public FloatExtractor getExtractor() {
			return extractor;
		}

		@Override public Object[] createArray(int lenght) {
			return new Float[lenght];
		}

		@Override public Object toPremitiveArray(Object obj) {
			return ArrayUtils.toPrimitive((Float[])obj);
		}

		@Override public Object toObjectArray(Object obj) {
			return ArrayUtils.toObject((float[])obj);
		}

		@Override public Class<?> getType() {
			return float.class;
		}
	};
	public static final PremitivType Boolean = new PremitivType() {
		BooleanExtractor extractor = new BooleanExtractor();
		
		@Override public Stream createStream(Object obj) {
			return new BooleanStream((boolean)obj);
		}

		@Override public BooleanExtractor getExtractor() {
			return extractor;
		}

		@Override public Object[] createArray(int lenght) {
			return new Boolean[lenght];
		}

		@Override public Object toPremitiveArray(Object obj) {
			return ArrayUtils.toPrimitive((Boolean[])obj);
		}

		@Override public Object toObjectArray(Object obj) {
			return ArrayUtils.toObject((boolean[])obj);
		}

		@Override public Class<?> getType() {
			return boolean.class;
		}
	};
	
	
	@Override public boolean isArray() {return false;}
}
