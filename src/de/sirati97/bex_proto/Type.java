package de.sirati97.bex_proto;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;

import de.sirati97.bex_proto.network.adv.SSCWrapperType;

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
	public static final Type String_ISO_8859_1 = new StringType(StandardCharsets.ISO_8859_1);
	public static final Type String_US_ASCII = new StringType(StandardCharsets.US_ASCII);
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
		
		public String getTypeName() {
			return "Integer";
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
		
		public String getTypeName() {
			return "Long";
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
		
		public String getTypeName() {
			return "Short";
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
		
		public String getTypeName() {
			return "Byte";
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
		
		public String getTypeName() {
			return "Double";
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
		
		public String getTypeName() {
			return "Float";
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
		public Object[] createArray(int lenght) {
			return new java.util.UUID[lenght];
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
		public Object[] createArray(int lenght) {
			return new java.net.InetAddress[lenght];
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
		public Object[] createArray(int lenght) {
			return new InetAddressPort[lenght];
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
		public Object[] createArray(int lenght) {
			return new TypeBase[lenght];
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
			return new DynamicObjStream((de.sirati97.bex_proto.DynamicObj) obj);
		}
		
		@Override
		public Object[] createArray(int lenght) {
			return new DynamicObj[lenght];
		}
	};
	
	@Override public boolean isArray() {return false;}
}
