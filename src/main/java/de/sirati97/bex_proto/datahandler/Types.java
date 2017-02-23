package de.sirati97.bex_proto.datahandler;

import de.sirati97.bex_proto.v1.network.adv.SSCWrapperType;
import org.apache.commons.lang3.ArrayUtils;

import java.io.Serializable;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class Types{
    private static final Map<String, IType> types = new HashMap<>();
    private static final CharsetEncoder ASCII_CHECKER = StandardCharsets.US_ASCII.newEncoder();



    public static IType get(String name) {
        return types.get(name);
    }

    public static IType getByInstance(Object obj, boolean platformIndependent, boolean subtypeNullable) {
        return getByInstance(obj, platformIndependent, subtypeNullable, null);
    }

    public static IType getByInstance(Object obj, boolean platformIndependent, boolean subtypeNullable, IType defaultType) {
        if(obj instanceof Object[]) {
            Class parentClazz = ((Object[]) obj).getClass().getComponentType();
            IType parentType = getByClazz(parentClazz, platformIndependent,subtypeNullable, defaultType);
            if (parentType == null) {
                return null;
            }

            if (!parentClazz.isPrimitive()) {
                parentType = parentType.asNullable();
            }
            return parentType.asArray();
        } else {
            for (IType type:types.values()) {
                if (type.isEncodable(obj, platformIndependent)) {
                    return type;
                }
            }
        }
        return defaultType;
    }


    public static IType getByClazz(Class clazz, boolean platformIndependent, boolean subtypeNullable) {
        return getByClazz(clazz, platformIndependent, subtypeNullable, null);
    }

    public static IType getByClazz(Class clazz, boolean platformIndependent, boolean subtypeNullable, IType defaultType) {
        if(clazz.isArray()) {
            Class parentClazz = clazz.getComponentType();
            IType parentType = getByClazz(parentClazz, platformIndependent,subtypeNullable, defaultType);
            if (parentType == null) {
                return null;
            }
            if (!parentClazz.isPrimitive()) {
                parentType = parentType.asNullable();
            }
            return parentType.asArray();
        } else {
            for (IType type:types.values()) {
                if (type.isEncodable(clazz, platformIndependent)) {
                    return type;
                }
            }
        }
        return defaultType;
    }

    public static void register(IType type) {
        if (type instanceof IDerivedType) {
            throw new IllegalArgumentException("Cannot register derived types. Please derive the from original Type");
        }
        String typeName = type.getTypeName();
        synchronized (ASCII_CHECKER) {
            if (!ASCII_CHECKER.canEncode(typeName)) {
                throw new IllegalStateException("Typename has to be ascii string");
            }
        }
        types.put(typeName, type);
    }

    //Types

    static void init() {}

    public static final SSCWrapperType SSCWrapper = new SSCWrapperType();
	public static final StringType String_Utf_8 = new StringType(StandardCharsets.UTF_8);
	public static final StringType String_Utf_16 = new StringType(StandardCharsets.UTF_16);
	public static final StringType String_Utf_16BE = new StringType(StandardCharsets.UTF_16BE);
	public static final StringType String_Utf_16LE = new StringType(StandardCharsets.UTF_16LE);
	public static final StringType String_ISO_8859_1 = new StringType(StandardCharsets.ISO_8859_1);
	public static final StringType String_US_ASCII = new StringType(StandardCharsets.US_ASCII);
	public static final PrimitiveType<Integer> Integer = new PrimitiveType<Integer>(new IntegerEncoder(), new IntegerDecoder()) {

        @Override public Integer castToPrimitive(Object obj) {
            return PrimitiveHelper.INSTANCE.toInt(obj);
        }

        @Override
        public boolean isEncodable(Object obj, boolean platformIndependent) {
            return PrimitiveHelper.INSTANCE.isInt(obj);
        }

        @Override
        public boolean isEncodable(Class clazz, boolean platformIndependent) {
            return PrimitiveHelper.INSTANCE.isInt(clazz);
        }


		@Override public Object toPrimitiveArray(Integer[] obj) {
			return ArrayUtils.toPrimitive(obj);
		}

		@Override public Integer[] toObjectArray(Object obj) {
			return ArrayUtils.toObject((int[])obj);
		}

        @Override public Class<?> getType() {
            return int.class;
        }

        @Override public Class<Integer> getObjType() {
            return Integer.class;
        }
		
		public String getTypeName() {
			return "Integer";
		}
	};
	public static final PrimitiveType<Long> Long = new PrimitiveType<Long>(new LongEncoder(), new LongDecoder()) {

        @Override public Long castToPrimitive(Object obj) {
            return PrimitiveHelper.INSTANCE.toLong(obj);
        }

        @Override
        public boolean isEncodable(Object obj, boolean platformIndependent) {
            return PrimitiveHelper.INSTANCE.isLong(obj);
        }

        @Override
        public boolean isEncodable(Class clazz, boolean platformIndependent) {
            return PrimitiveHelper.INSTANCE.isLong(clazz);
        }


		@Override public Object toPrimitiveArray(Long[] obj) {
			return ArrayUtils.toPrimitive(obj);
		}

		@Override public Long[] toObjectArray(Object obj) {
			return ArrayUtils.toObject((long[])obj);
		}

		@Override public Class<?> getType() {
			return long.class;
		}

        @Override
        public Class<Long> getObjType() {
            return Long.class;
        }

        public String getTypeName() {
			return "Long";
		}
	};
	public static final PrimitiveType<Short> Short = new PrimitiveType<Short>(new ShortEncoder(), new ShortDecoder()) {

        @Override public Short castToPrimitive(Object obj) {
            return PrimitiveHelper.INSTANCE.toShort(obj);
        }

        @Override
        public boolean isEncodable(Object obj, boolean platformIndependent) {
            return PrimitiveHelper.INSTANCE.isShort(obj);
        }

        @Override
        public boolean isEncodable(Class clazz, boolean platformIndependent) {
            return PrimitiveHelper.INSTANCE.isShort(clazz);
        }

		@Override public Object toPrimitiveArray(Short[] obj) {
			return ArrayUtils.toPrimitive(obj);
		}

		@Override public Short[] toObjectArray(Object obj) {
			return ArrayUtils.toObject((short[])obj);
		}

		@Override public Class<?> getType() {
			return short.class;
		}

        @Override
        public Class<Short> getObjType() {
            return Short.class;
        }

        public String getTypeName() {
			return "Short";
		}
	};
	public static final PrimitiveType<Byte> Byte = new PrimitiveType<Byte>(new ByteEncoder(), new ByteDecoder()) {

        @Override public Byte castToPrimitive(Object obj) {
            return PrimitiveHelper.INSTANCE.toByte(obj);
        }

        @Override
        public boolean isEncodable(Object obj, boolean platformIndependent) {
            return PrimitiveHelper.INSTANCE.isByte(obj);
        }

        @Override
        public boolean isEncodable(Class clazz, boolean platformIndependent) {
            return PrimitiveHelper.INSTANCE.isByte(clazz);
        }

		@Override public Object toPrimitiveArray(Byte[] obj) {
			return ArrayUtils.toPrimitive(obj);
		}

		@Override public Byte[] toObjectArray(Object obj) {
			return ArrayUtils.toObject((byte[])obj);
		}

		@Override public Class<?> getType() {
			return byte.class;
		}

        @Override
        public Class<Byte> getObjType() {
            return Byte.class;
        }

        public String getTypeName() {
			return "Byte";
		}
	};
	public static final PrimitiveType<Double> Double = new PrimitiveType<Double>(new DoubleEncoder(), new DoubleDecoder()) {

        @Override public Double castToPrimitive(Object obj) {
            return PrimitiveHelper.INSTANCE.toDouble(obj);
        }

        @Override
        public boolean isEncodable(Object obj, boolean platformIndependent) {
            return PrimitiveHelper.INSTANCE.isDouble(obj);
        }

        @Override
        public boolean isEncodable(Class clazz, boolean platformIndependent) {
            return PrimitiveHelper.INSTANCE.isDouble(clazz);
        }

		@Override public Object toPrimitiveArray(Double[] obj) {
			return ArrayUtils.toPrimitive(obj);
		}

		@Override public Double[] toObjectArray(Object obj) {
			return ArrayUtils.toObject((double[])obj);
		}

		@Override public Class<?> getType() {
			return double.class;
		}

        @Override
        public Class<Double> getObjType() {
            return Double.class;
        }

        public String getTypeName() {
			return "Double";
		}
	};
	public static final PrimitiveType<Float> Float = new PrimitiveType<Float>(new FloatEncoder(), new FloatDecoder()) {

        @Override public Float castToPrimitive(Object obj) {
            return PrimitiveHelper.INSTANCE.toFloat(obj);
        }

        @Override
        public boolean isEncodable(Object obj, boolean platformIndependent) {
            return PrimitiveHelper.INSTANCE.isFloat(obj);
        }

        @Override
        public boolean isEncodable(Class clazz, boolean platformIndependent) {
            return PrimitiveHelper.INSTANCE.isFloat(clazz);
        }

		@Override public Object toPrimitiveArray(Float[] obj) {
			return ArrayUtils.toPrimitive(obj);
		}

		@Override public Float[] toObjectArray(Object obj) {
			return ArrayUtils.toObject((float[])obj);
		}

		@Override public Class<?> getType() {
			return float.class;
		}

        @Override
        public Class<Float> getObjType() {
            return Float.class;
        }

        public String getTypeName() {
			return "Float";
		}
	};
	public static final BooleanType Boolean = new BooleanType();
	public static final ObjType<java.util.UUID> UUID = new ObjType<java.util.UUID>(new UUIDEncoder(), new UUIDDecoder()) {
		
		@Override
		public Class<UUID> getType() {
			return java.util.UUID.class;
		}

        @Override
        public boolean isEncodable(Object obj, boolean platformIndependent) {
            return obj instanceof UUID;
        }
		
		public String getTypeName() {
			return "UUID";
		}
	};
	public static final ObjType<java.net.InetAddress> InetAddress = new ObjType<java.net.InetAddress>(new InetAddressEncoder(), new InetAddressDecoder()) {
		
		@Override
		public Class<java.net.InetAddress> getType() {
			return java.net.InetAddress.class;
		}

        @Override
        public boolean isEncodable(Object obj, boolean platformIndependent) {
            return obj instanceof java.net.InetAddress;
        }

        @Override
        public boolean isEncodable(Class clazz, boolean platformIndependent) {
            return PrimitiveHelper.INSTANCE.isInt(clazz);
        }
		
		public String getTypeName() {
			return "InetAddress";
		}
	};
	public static final ObjType<InetAddressPort> InetAddressPort = new ObjType<InetAddressPort>(new InetAddressPortEncoder(), new InetAddressPortDecoder()) {
		
		@Override
		public Class<InetAddressPort> getType() {
			return InetAddressPort.class;
		}

        @Override
        public boolean isEncodable(Object obj, boolean platformIndependent) {
            return obj instanceof InetAddressPort;
        }
		
		public String getTypeName() {
			return "InetAddressPort";
		}
	};
	public static final ObjType<IType> Type = new ObjType<IType>(new TypeEncoder(), new TypeDecoder()) {
		
		@Override
		public String getTypeName() {
			return "Type";
		}
		
		@Override
		public Class<IType> getType() {
			return IType.class;
		}

        @Override
        public boolean isEncodable(Object obj, boolean platformIndependent) {
            return obj instanceof IType;
        }
	};
	public static final ObjType<DynamicObj> DynamicObj = new ObjType<DynamicObj>(new DynamicObjEncoder(), new DynamicObjDecoder()) {
		DynamicObjDecoder decoder = new DynamicObjDecoder();
		
		@Override
		public String getTypeName() {
			return "DynamicObj";
		}
		
		@Override
		public Class<DynamicObj> getType() {
			return DynamicObj.class;
		}

        @Override
        public boolean isEncodable(Object obj, boolean platformIndependent) {
            return obj instanceof DynamicObj;
        }
	};
	public static final JavaSerializableType<Serializable> JavaSerializable = new JavaSerializableType<>(Serializable.class, "JavaSerializable", true);
	public static final JavaSerializableType<Throwable> JavaThrowable = new JavaSerializableType<>(Throwable.class, "JavaThrowable", true);
}
