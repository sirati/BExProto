package de.sirati97.bex_proto.datahandler;

import de.sirati97.bex_proto.v1.network.adv.SSCWrapperType;
import org.apache.commons.lang3.ArrayUtils;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class Type<T> implements TypeBase<T>{
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

	public abstract Stream createStreamCasted(T obj);

	@Override
	public final Stream createStream(Object obj) {
		return createStreamCasted((T) obj);
	}

	public static final SSCWrapperType SSCWrapper = new SSCWrapperType();
	public static final StringType String_Utf_8 = new StringType(StandardCharsets.UTF_8);
	public static final StringType String_Utf_16 = new StringType(StandardCharsets.UTF_16);
	public static final StringType String_Utf_16BE = new StringType(StandardCharsets.UTF_16BE);
	public static final StringType String_Utf_16LE = new StringType(StandardCharsets.UTF_16LE);
	public static final StringType String_ISO_8859_1 = new StringType(StandardCharsets.ISO_8859_1);
	public static final StringType String_US_ASCII = new StringType(StandardCharsets.US_ASCII);
	public static final PrimitiveType<Integer> Integer = new PrimitiveType<Integer>() {
		IntegerExtractor extractor = new IntegerExtractor();
		
		@Override public Stream createStreamCasted(Integer obj) {
			return new IntegerStream(obj);
		}

		@Override public IntegerExtractor getExtractor() {
			return extractor;
		}

		@Override public Integer[] createArray(int length) {
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
	public static final PrimitiveType<Long> Long = new PrimitiveType<Long>() {
		LongExtractor extractor = new LongExtractor();
		
		@Override public Stream createStreamCasted(Long obj) {
			return new LongStream(obj);
		}

		@Override public LongExtractor getExtractor() {
			return extractor;
		}

		@Override public Long[] createArray(int length) {
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
	public static final PrimitiveType<Short> Short = new PrimitiveType<Short>() {
		ShortExtractor extractor = new ShortExtractor();
		
		@Override public Stream createStreamCasted(Short obj) {
			return new ShortStream(obj);
		}

		@Override public ShortExtractor getExtractor() {
			return extractor;
		}

		@Override public Short[] createArray(int length) {
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
	public static final PrimitiveType<Byte> Byte = new PrimitiveType<Byte>() {
		ByteExtractor extractor = new ByteExtractor();
		
		@Override public Stream createStreamCasted(Byte obj) {
			return new ByteStream(obj);
		}

		@Override public ByteExtractor getExtractor() {
			return extractor;
		}

		@Override public Byte[] createArray(int length) {
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
	public static final PrimitiveType<Double> Double = new PrimitiveType<Double>() {
		DoubleExtractor extractor = new DoubleExtractor();
		
		@Override public Stream createStreamCasted(Double obj) {
			return new DoubleStream(obj);
		}

		@Override public DoubleExtractor getExtractor() {
			return extractor;
		}

		@Override public Double[] createArray(int length) {
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
	public static final PrimitiveType<Float> Float = new PrimitiveType<Float>() {
		FloatExtractor extractor = new FloatExtractor();
		
		@Override public Stream createStreamCasted(Float obj) {
			return new FloatStream(obj);
		}

		@Override public FloatExtractor getExtractor() {
			return extractor;
		}

		@Override public Float[] createArray(int length) {
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
	public static final PrimitiveType<Boolean> Boolean = new PrimitiveType<Boolean>() {
		BooleanExtractor extractor = new BooleanExtractor();
		
		@Override public Stream createStreamCasted(Boolean obj) {
			return new BooleanStream(obj);
		}

		@Override public BooleanExtractor getExtractor() {
			return extractor;
		}

		@Override public Boolean[] createArray(int length) {
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
	public static final Type<java.util.UUID> UUID = new ObjType<java.util.UUID>() {
		UUIDExtractor extractor = new UUIDExtractor();
		
		@Override
		public Class<UUID> getType() {
			return java.util.UUID.class;
		}
		
		@Override
		public StreamExtractor<UUID> getExtractor() {
			return extractor;
		}
		
		@Override
		public Stream createStreamCasted(UUID obj) {
			return new UUIDStream(obj);
		}
		
		@Override
		public UUID[] createArray(int length) {
			return new java.util.UUID[length];
		}
		
		public String getTypeName() {
			return "UUID";
		}
	};
	public static final Type<java.net.InetAddress> InetAddress = new ObjType<java.net.InetAddress>() {
		InetAddressExtractor extractor = new InetAddressExtractor();
		
		@Override
		public Class<java.net.InetAddress> getType() {
			return java.net.InetAddress.class;
		}
		
		@Override
		public StreamExtractor<java.net.InetAddress> getExtractor() {
			return extractor;
		}
		
		@Override
		public Stream createStreamCasted(java.net.InetAddress obj) {
			return new InetAddressStream(obj);
		}
		
		@Override
		public java.net.InetAddress[] createArray(int length) {
			return new java.net.InetAddress[length];
		}
		
		public String getTypeName() {
			return "InetAddress";
		}
	};
	public static final Type<InetAddressPort> InetAddressPort = new ObjType<InetAddressPort>() {
		InetAddressPortExtractor extractor = new InetAddressPortExtractor();
		
		@Override
		public Class<InetAddressPort> getType() {
			return InetAddressPort.class;
		}
		
		@Override
		public StreamExtractor<InetAddressPort> getExtractor() {
			return extractor;
		}
		
		@Override
		public Stream createStreamCasted(InetAddressPort obj) {
			return new InetAddressPortStream( obj);
		}
		
		@Override
		public InetAddressPort[] createArray(int length) {
			return new InetAddressPort[length];
		}
		
		public String getTypeName() {
			return "InetAddressPort";
		}
	};
	public static final ObjType<TypeBase> Type = new ObjType<TypeBase>() {
		TypeExtractor extractor = new TypeExtractor();
		
		@Override
		public String getTypeName() {
			return "Type";
		}
		
		@Override
		public Class<TypeBase> getType() {
			return TypeBase.class;
		}
		
		@Override
		public StreamExtractor<TypeBase> getExtractor() {
			return extractor;
		}
		
		@Override
		public Stream createStreamCasted(TypeBase obj) {
			return new TypeStream(obj);
		}
		
		@Override
		public TypeBase[] createArray(int length) {
			return new TypeBase[length];
		}
	};
	public static final ObjType<DynamicObj> DynamicObj = new ObjType<DynamicObj>() {
		DynamicObjExtractor extractor = new DynamicObjExtractor();
		
		@Override
		public String getTypeName() {
			return "DynamicObj";
		}
		
		@Override
		public Class<DynamicObj> getType() {
			return DynamicObj.class;
		}
		
		@Override
		public StreamExtractor<DynamicObj> getExtractor() {
			return extractor;
		}
		
		@Override
		public Stream createStreamCasted(DynamicObj obj) {
			return new DynamicObjStream(obj);
		}
		
		@Override
		public DynamicObj[] createArray(int length) {
			return new DynamicObj[length];
		}
	};
	public static final JavaSerializableType<Serializable> JavaSerializable = new JavaSerializableType<>(Serializable.class, "JavaSerializable", true);
	public static final JavaSerializableType<Throwable> JavaThrowable = new JavaSerializableType<>(Throwable.class, "JavaThrowable", true);
	
	@Override public boolean isArray() {return false;}
}
