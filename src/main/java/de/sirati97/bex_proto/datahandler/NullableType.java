package de.sirati97.bex_proto.datahandler;


public class NullableType<T> extends DerivedType<T,T> implements INullableType<T> {
	private IType<T> type;


	public NullableType(IType<T> type) {
		this(IDerivedType.Register.NULLABLE_TYPE_FACTORY, type);
	}

    public NullableType(NullableTypeFactory factory, IType<T> type) {
        this(new NullableEncoder<T>(type), new NullableDecoder<T>(type), factory, type);
    }

    public NullableType(IEncoder<T> encoder, IDecoder<T> decoder, IType<T> type) {
        this(encoder, decoder, IDerivedType.Register.NULLABLE_TYPE_FACTORY, type);
    }
    public NullableType(IEncoder<T> encoder, IDecoder<T> decoder, NullableTypeFactory factory, IType<T> type) {
		super(encoder, decoder, factory);
		this.type = type;
	}

	@Override
	public boolean isArray() {
		return getInnerType().isArray();
	}

	@Override
	public boolean isPrimitive() {
		return false;
	}

    @Override
    public boolean isEncodable(Object obj, boolean platformIndependent) {
        return type.isEncodable(obj, platformIndependent);
    }

    @Override
    public boolean isEncodable(Class paramClass, boolean platformIndependent) {
        return type.isEncodable(paramClass, platformIndependent);
    }

	@Override
	public T[] createArray(int length) {
		return getInnerType().createArray(length);
	}

	@Override
	public Class<T> getType() {
		return getInnerType().getObjType();
	}
	
	public IType<T> getInnerType() {
		return type;
	}

	@Override
	public boolean equals(Object arg0) {
		if (arg0 instanceof NullableType) {
			return getInnerType().equals(((NullableType)arg0).getInnerType());
		}
		return super.equals(arg0);
	}
	

	@Override
	public String getTypeName() {
		return "Nullable<"+getInnerType().getTypeName()+">";
	}

	@Override
	public IArrayType getInnerArray() {
		if (isArray()) {
			if (getInnerType() instanceof IArrayType) {
				return (IArrayType) getInnerType();
			} else if (getInnerType() instanceof IDerivedType) {
				return ((IDerivedType)getInnerType()).getInnerArray();
			} else {
				throw new IllegalStateException("Inner type is no array, but isArray() returns true!");
			}
		} else {
			throw new IllegalArgumentException(new IllegalAccessException("Inner Type is no array!"));
		}
	}

	@Override
	public boolean isBasePrimitive() {
		if (getInnerType().isPrimitive()) {
			return false;
		} else {
			if (getInnerType() instanceof IDerivedType) {
				return ((IDerivedType)getInnerType()).isBasePrimitive();
			} else {
				return false;
			}
		}
	}


	@Override
	protected IArrayType<T> createArrayType() {
		return new ArrayNullableType<>(this);
	}

	@Override
	protected INullableType<T> createNullableType() {
		return this;
	}
}
