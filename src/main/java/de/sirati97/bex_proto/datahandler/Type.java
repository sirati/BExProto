package de.sirati97.bex_proto.datahandler;

import de.sirati97.bex_proto.v1.network.adv.SSCWrapperType;
import org.apache.commons.lang3.ArrayUtils;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public abstract class Type implements TypeBase{
	private static Map<String, TypeBase> types = new HashMap<>();
	
	public Type() {
		if(earlyRegister())register();
	}
	
	public static TypeBase get(String name) {
		return types.get(name);
	}
	
	protected boolean earlyRegister() {
		return true;
	}
	
	protected void register() {
		types.put(getTypeName(), this);
	}
	
	public static final Type SSCWrapper = new SSCWrapperType();
	public static final Type String_Utf_8 = new StringType(StandardCharsets.UTF_8);
	public static final Type String_Utf_16 = new StringType(StandardCharsets.UTF_16);
	public static final Type String_Utf_16BE = new StringType(StandardCharsets.UTF_16BE);
	public static final Type String_Utf_16LE = new StringType(StandardCharsets.UTF_16LE);
	public static final Type String_ISO_8859_1 = new StringType(StandardCharsets.ISO_8859_1);
	public static final Type String_US_ASCII = new StringType(StandardCharsets.US_ASCII);
	public static final PrimitiveType Integer = new PrimitiveType() {
		IntegerExtractor extractor = new IntegerExtractor();
		
		@Override public Stream createStream(Object obj) {
			return new IntegerStream((int)obj);
		}

		@Override public IntegerExtractor getExtractor() {
			return extractor;
		}

		@Override public Object[] createArray(int length) {
			return new Integer[length];
		}

		@Override public Object toPrimitiveArray(Object obj) {
			return ArrayUtils.toPrimitive((Integer[])obj);
		}

		@Override public Object toObjectArray(Object obj) {
			return ArrayUtils.toObject((int[])obj);
		}

		@Override public Class<?> getType() {
			return int.class;
		}
		
		public String getTypeName() {
			return "Integer";
		}
	};
	public static final PrimitiveType Long = new PrimitiveType() {
		LongExtractor extractor = new LongExtractor();
		
		@Override public Stream createStream(Object obj) {
			return new LongStream((long)obj);
		}

		@Override public LongExtractor getExtractor() {
			return extractor;
		}

		@Override public Object[] createArray(int length) {
			return new Long[length];
		}

		@Override public Object toPrimitiveArray(Object obj) {
			return ArrayUtils.toPrimitive((Long[])obj);
		}

		@Override public Object toObjectArray(Object obj) {
			return ArrayUtils.toObject((long[])obj);
		}

		@Override public Class<?> getType() {
			return long.class;
		}
		
		public String getTypeName() {
			return "Long";
		}
	};
	public static final PrimitiveType Short = new PrimitiveType() {
		ShortExtractor extractor = new ShortExtractor();
		
		@Override public Stream createStream(Object obj) {
			return new ShortStream((short)obj);
		}

		@Override public ShortExtractor getExtractor() {
			return extractor;
		}

		@Override public Object[] createArray(int length) {
			return new Short[length];
		}

		@Override public Object toPrimitiveArray(Object obj) {
			return ArrayUtils.toPrimitive((Short[])obj);
		}

		@Override public Object toObjectArray(Object obj) {
			return ArrayUtils.toObject((short[])obj);
		}

		@Override public Class<?> getType() {
			return short.class;
		}
		
		public String getTypeName() {
			return "Short";
		}
	};
	public static final PrimitiveType Byte = new PrimitiveType() {
		ByteExtractor extractor = new ByteExtractor();
		
		@Override public Stream createStream(Object obj) {
			return new ByteStream((byte)obj);
		}

		@Override public ByteExtractor getExtractor() {
			return extractor;
		}

		@Override public Object[] createArray(int length) {
			return new Byte[length];
		}

		@Override public Object toPrimitiveArray(Object obj) {
			return ArrayUtils.toPrimitive((Byte[])obj);
		}

		@Override public Object toObjectArray(Object obj) {
			return ArrayUtils.toObject((byte[])obj);
		}

		@Override public Class<?> getType() {
			return byte.class;
		}
		
		public String getTypeName() {
			return "Byte";
		}
	};
	public static final PrimitiveType Double = new PrimitiveType() {
		DoubleExtractor extractor = new DoubleExtractor();
		
		@Override public Stream createStream(Object obj) {
			return new DoubleStream((double)obj);
		}

		@Override public DoubleExtractor getExtractor() {
			return extractor;
		}

		@Override public Object[] createArray(int length) {
			return new Double[length];
		}

		@Override public Object toPrimitiveArray(Object obj) {
			return ArrayUtils.toPrimitive((Double[])obj);
		}

		@Override public Object toObjectArray(Object obj) {
			return ArrayUtils.toObject((double[])obj);
		}

		@Override public Class<?> getType() {
			return double.class;
		}
		
		public String getTypeName() {
			return "Double";
		}
	};
	public static final PrimitiveType Float = new PrimitiveType() {
		FloatExtractor extractor = new FloatExtractor();
		
		@Override public Stream createStream(Object obj) {
			return new FloatStream((float)obj);
		}

		@Override public FloatExtractor getExtractor() {
			return extractor;
		}

		@Override public Object[] createArray(int length) {
			return new Float[length];
		}

		@Override public Object toPrimitiveArray(Object obj) {
			return ArrayUtils.toPrimitive((Float[])obj);
		}

		@Override public Object toObjectArray(Object obj) {
			return ArrayUtils.toObject((float[])obj);
		}

		@Override public Class<?> getType() {
			return float.class;
		}
		
		public String getTypeName() {
			return "Float";
		}
	};
	public static final PrimitiveType Boolean = new PrimitiveType() {
		BooleanExtractor extractor = new BooleanExtractor();
		
		@Override public Stream createStream(Object obj) {
			return new BooleanStream((boolean)obj);
		}

		@Override public BooleanExtractor getExtractor() {
			return extractor;
		}

		@Override public Object[] createArray(int length) {
			return new Boolean[length];
		}

		@Override public Object toPrimitiveArray(Object obj) {
			return ArrayUtils.toPrimitive((Boolean[])obj);
		}

		@Override public Object toObjectArray(Object obj) {
			return ArrayUtils.toObject((boolean[])obj);
		}

		@Override public Class<?> getType() {
			return boolean.class;
		}
		
		public String getTypeName() {
			return "Boolean";
		}
	};
	public static final Type UUID = new ObjType() {
		UUIDExtractor extractor = new UUIDExtractor();
		
		@Override
		public Class<?> getType() {
			return java.util.UUID.class;
		}
		
		@Override
		public StreamExtractor<? extends Object> getExtractor() {
			return extractor;
		}
		
		@Override
		public Stream createStream(Object obj) {
			return new UUIDStream((java.util.UUID) obj);
		}
		
		@Override
		public Object[] createArray(int length) {
			return new java.util.UUID[length];
		}
		
		public String getTypeName() {
			return "UUID";
		}
	};
	public static final Type InetAddress = new ObjType() {
		InetAddressExtractor extractor = new InetAddressExtractor();
		
		@Override
		public Class<?> getType() {
			return java.net.InetAddress.class;
		}
		
		@Override
		public StreamExtractor<? extends Object> getExtractor() {
			return extractor;
		}
		
		@Override
		public Stream createStream(Object obj) {
			return new InetAddressStream((java.net.InetAddress) obj);
		}
		
		@Override
		public Object[] createArray(int length) {
			return new java.net.InetAddress[length];
		}
		
		public String getTypeName() {
			return "InetAddress";
		}
	};
	public static final Type InetAddressPort = new ObjType() {
		InetAddressPortExtractor extractor = new InetAddressPortExtractor();
		
		@Override
		public Class<?> getType() {
			return InetAddressPort.class;
		}
		
		@Override
		public StreamExtractor<? extends Object> getExtractor() {
			return extractor;
		}
		
		@Override
		public Stream createStream(Object obj) {
			return new InetAddressPortStream((InetAddressPort) obj);
		}
		
		@Override
		public Object[] createArray(int length) {
			return new InetAddressPort[length];
		}
		
		public String getTypeName() {
			return "InetAddressPort";
		}
	};
	public static final ObjType Type = new ObjType() {
		TypeExtractor extractor = new TypeExtractor();
		
		@Override
		public String getTypeName() {
			return "Type";
		}
		
		@Override
		public Class<?> getType() {
			return TypeBase.class;
		}
		
		@Override
		public StreamExtractor<? extends Object> getExtractor() {
			return extractor;
		}
		
		@Override
		public Stream createStream(Object obj) {
			return new TypeStream((TypeBase) obj);
		}
		
		@Override
		public Object[] createArray(int length) {
			return new TypeBase[length];
		}
	};
	public static final ObjType DynamicObj = new ObjType() {
		DynamicObjExtractor extractor = new DynamicObjExtractor();
		
		@Override
		public String getTypeName() {
			return "DynamicObj";
		}
		
		@Override
		public Class<?> getType() {
			return DynamicObj.class;
		}
		
		@Override
		public StreamExtractor<? extends Object> getExtractor() {
			return extractor;
		}
		
		@Override
		public Stream createStream(Object obj) {
			return new DynamicObjStream((de.sirati97.bex_proto.datahandler.DynamicObj) obj);
		}
		
		@Override
		public Object[] createArray(int length) {
			return new DynamicObj[length];
		}
	};
	public static final JavaSerializableType<Serializable> JavaSerializable = new JavaSerializableType<>(Serializable.class, "JavaSerializable", true);
	public static final JavaSerializableType<Throwable> JavaThrowable = new JavaSerializableType<>(Throwable.class, "JavaThrowable", true);
	
	@Override public boolean isArray() {return false;}
}
