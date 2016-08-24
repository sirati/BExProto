package de.sirati97.bex_proto.datahandler;

import de.sirati97.bex_proto.v1.network.adv.SSCWrapperType;
import org.apache.commons.lang3.ArrayUtils;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class Type<T> implements TypeBase<T>{
	private static final Map<String, TypeBase> types = new HashMap<>();
    private static final CharsetEncoder ASCII_CHECKER = StandardCharsets.US_ASCII.newEncoder();
	private INullableType<T> nullableType;
	private IArrayType<T> arrayType;


	public Type() {
		if(earlyRegister())register();
	}
	
	public static TypeBase get(String name) {
		return types.get(name);
	}

    public static TypeBase getByInstance(Object obj, boolean platformIndependent, boolean subtypeNullable) {
        return getByInstance(obj, platformIndependent, subtypeNullable, null);
    }

    public static TypeBase getByInstance(Object obj, boolean platformIndependent, boolean subtypeNullable, TypeBase defaultType) {
        if(obj instanceof Object[]) {
            Class parentClazz = ((Object[]) obj).getClass().getComponentType();
            TypeBase parentType = getByClazz(parentClazz, platformIndependent,subtypeNullable, defaultType);
            if (parentType == null) {
                return null;
            }

            if (!parentClazz.isPrimitive()) {
                parentType = parentType.asNullable();
            }
            return parentType.asArray();
        } else {
            for (TypeBase type:types.values()) {
                if (type.isEncodable(obj, platformIndependent)) {
                    return type;
                }
            }
        }
        return defaultType;
    }


    public static TypeBase getByClazz(Class clazz, boolean platformIndependent, boolean subtypeNullable) {
        return getByClazz(clazz, platformIndependent, subtypeNullable, null);
    }

    public static TypeBase getByClazz(Class clazz, boolean platformIndependent, boolean subtypeNullable, TypeBase defaultType) {
        if(clazz.isArray()) {
            Class parentClazz = clazz.getComponentType();
            TypeBase parentType = getByClazz(parentClazz, platformIndependent,subtypeNullable, defaultType);
            if (parentType == null) {
                return null;
            }
            if (!parentClazz.isPrimitive()) {
                parentType = parentType.asNullable();
            }
            return parentType.asArray();
        } else {
            for (TypeBase type:types.values()) {
                if (type.isEncodable(clazz, platformIndependent)) {
                    return type;
                }
            }
        }
        return defaultType;
    }
	
	protected boolean earlyRegister() {
		return true;
	}
	
	protected void register() {
        String typeName = getTypeName();
        synchronized (ASCII_CHECKER) {
            if (!ASCII_CHECKER.canEncode(typeName)) {
                throw new IllegalStateException("Typename has to be ascii string");
            }
        }
		types.put(typeName, this);
	}

	public abstract Stream createStreamCasted(T obj);

    protected IArrayType<T> createArrayType() {
        return new ArrayType<>(this);
    }

    protected INullableType<T> createNullableType() {
        return new NullableType<>(this);
    }

	@Override
	public final INullableType<T> asNullable() {
		return nullableType==null?(nullableType=createNullableType()):nullableType;
	}

	@Override
	public final IArrayType<T> asArray() {
		return arrayType==null?(arrayType=createArrayType()):arrayType;
	}


    @SuppressWarnings("unchecked")
    @Override
    public T[] createArray(int length) {
        return (T[]) Array.newInstance(getObjType(), length);
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

        @Override public Integer castToPrimitive(Object obj) {
            return PrimitiveHelper.INSTANCE.toInt(obj);
        }

        @Override public Stream createStreamCasted(Integer obj) {
			return new IntegerStream(obj);
		}

        @Override
        public boolean isEncodable(Object obj, boolean platformIndependent) {
            return PrimitiveHelper.INSTANCE.isInt(obj);
        }

        @Override
        public boolean isEncodable(Class clazz, boolean platformIndependent) {
            return PrimitiveHelper.INSTANCE.isInt(clazz);
        }

        @Override public IntegerExtractor getExtractor() {
			return extractor;
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

        @Override public Class<Integer> getObjType() {
            return Integer.class;
        }
		
		public String getTypeName() {
			return "Integer";
		}
	};
	public static final PrimitiveType<Long> Long = new PrimitiveType<Long>() {
		LongExtractor extractor = new LongExtractor();

        @Override public Long castToPrimitive(Object obj) {
            return PrimitiveHelper.INSTANCE.toLong(obj);
        }
		
		@Override public Stream createStreamCasted(Long obj) {
			return new LongStream(obj);
		}

        @Override
        public boolean isEncodable(Object obj, boolean platformIndependent) {
            return PrimitiveHelper.INSTANCE.isLong(obj);
        }

        @Override
        public boolean isEncodable(Class clazz, boolean platformIndependent) {
            return PrimitiveHelper.INSTANCE.isLong(clazz);
        }

		@Override public LongExtractor getExtractor() {
			return extractor;
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

        @Override
        public Class<Long> getObjType() {
            return Long.class;
        }

        public String getTypeName() {
			return "Long";
		}
	};
	public static final PrimitiveType<Short> Short = new PrimitiveType<Short>() {
		ShortExtractor extractor = new ShortExtractor();

        @Override public Short castToPrimitive(Object obj) {
            return PrimitiveHelper.INSTANCE.toShort(obj);
        }
		
		@Override public Stream createStreamCasted(Short obj) {
			return new ShortStream(obj);
		}

        @Override
        public boolean isEncodable(Object obj, boolean platformIndependent) {
            return PrimitiveHelper.INSTANCE.isShort(obj);
        }

        @Override
        public boolean isEncodable(Class clazz, boolean platformIndependent) {
            return PrimitiveHelper.INSTANCE.isShort(clazz);
        }

		@Override public ShortExtractor getExtractor() {
			return extractor;
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

        @Override
        public Class<Short> getObjType() {
            return Short.class;
        }

        public String getTypeName() {
			return "Short";
		}
	};
	public static final PrimitiveType<Byte> Byte = new PrimitiveType<Byte>() {
		ByteExtractor extractor = new ByteExtractor();

        @Override public Byte castToPrimitive(Object obj) {
            return PrimitiveHelper.INSTANCE.toByte(obj);
        }
		
		@Override public Stream createStreamCasted(Byte obj) {
			return new ByteStream(obj);
		}

        @Override
        public boolean isEncodable(Object obj, boolean platformIndependent) {
            return PrimitiveHelper.INSTANCE.isByte(obj);
        }

        @Override
        public boolean isEncodable(Class clazz, boolean platformIndependent) {
            return PrimitiveHelper.INSTANCE.isByte(clazz);
        }

		@Override public ByteExtractor getExtractor() {
			return extractor;
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

        @Override
        public Class<Byte> getObjType() {
            return Byte.class;
        }

        public String getTypeName() {
			return "Byte";
		}
	};
	public static final PrimitiveType<Double> Double = new PrimitiveType<Double>() {
		DoubleExtractor extractor = new DoubleExtractor();

        @Override public Double castToPrimitive(Object obj) {
            return PrimitiveHelper.INSTANCE.toDouble(obj);
        }
		
		@Override public Stream createStreamCasted(Double obj) {
			return new DoubleStream(obj);
		}

        @Override
        public boolean isEncodable(Object obj, boolean platformIndependent) {
            return PrimitiveHelper.INSTANCE.isDouble(obj);
        }

        @Override
        public boolean isEncodable(Class clazz, boolean platformIndependent) {
            return PrimitiveHelper.INSTANCE.isDouble(clazz);
        }

		@Override public DoubleExtractor getExtractor() {
			return extractor;
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

        @Override
        public Class<Double> getObjType() {
            return Double.class;
        }

        public String getTypeName() {
			return "Double";
		}
	};
	public static final PrimitiveType<Float> Float = new PrimitiveType<Float>() {
		FloatExtractor extractor = new FloatExtractor();

        @Override public Float castToPrimitive(Object obj) {
            return PrimitiveHelper.INSTANCE.toFloat(obj);
        }
		
		@Override public Stream createStreamCasted(Float obj) {
			return new FloatStream(obj);
		}

        @Override
        public boolean isEncodable(Object obj, boolean platformIndependent) {
            return PrimitiveHelper.INSTANCE.isFloat(obj);
        }

        @Override
        public boolean isEncodable(Class clazz, boolean platformIndependent) {
            return PrimitiveHelper.INSTANCE.isFloat(clazz);
        }

		@Override public FloatExtractor getExtractor() {
			return extractor;
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

        @Override
        public Class<Float> getObjType() {
            return Float.class;
        }

        public String getTypeName() {
			return "Float";
		}
	};
	public static final BooleanType Boolean = new BooleanType();
	public static final ObjType<java.util.UUID> UUID = new ObjType<java.util.UUID>() {
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
        public boolean isEncodable(Object obj, boolean platformIndependent) {
            return obj instanceof UUID;
        }
		
		public String getTypeName() {
			return "UUID";
		}
	};
	public static final ObjType<java.net.InetAddress> InetAddress = new ObjType<java.net.InetAddress>() {
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
	public static final ObjType<InetAddressPort> InetAddressPort = new ObjType<InetAddressPort>() {
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
        public boolean isEncodable(Object obj, boolean platformIndependent) {
            return obj instanceof InetAddressPort;
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
        public boolean isEncodable(Object obj, boolean platformIndependent) {
            return obj instanceof TypeBase;
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
        public boolean isEncodable(Object obj, boolean platformIndependent) {
            return obj instanceof DynamicObj;
        }
	};
	public static final JavaSerializableType<Serializable> JavaSerializable = new JavaSerializableType<>(Serializable.class, "JavaSerializable", true);
	public static final JavaSerializableType<Throwable> JavaThrowable = new JavaSerializableType<>(Throwable.class, "JavaThrowable", true);
	
	@Override public boolean isArray() {return false;}
}
