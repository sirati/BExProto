package de.sirati97.bex_proto.datahandler;


public class NullableType<T> extends DerivedType<T,T> {
	private TypeBase<T> type;
	private StreamExtractor<T> extractor;


	public NullableType(TypeBase<T> type) {
		this(DerivedTypeBase.Register.NULLABLE_TYPE_FACTORY, type);
	}
	
	public NullableType(NullableTypeFactory factory, TypeBase type) {
		super(factory);
		this.type = type;
		this.extractor = new NullableExtractor(type);
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
	public Stream createStream(Object obj) {
		return new NullableStream(getInnerType(), obj);
	}

	@Override
	public StreamExtractor<T> getExtractor() {
		return extractor;
	}

	@Override
	public T[] createArray(int length) {
		return getInnerType().createArray(length);
	}

	@Override
	public Class<?> getType() {
		return getInnerType().getClass();
	}
	
	public TypeBase<T> getInnerType() {
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
	public ArrayType getInnerArray() {
		if (isArray()) {
			if (getInnerType() instanceof ArrayType) {
				return (ArrayType) getInnerType();
			} else if (getInnerType() instanceof DerivedTypeBase) {
				return ((DerivedTypeBase)getInnerType()).getInnerArray();
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
			if (getInnerType() instanceof DerivedTypeBase) {
				return ((DerivedTypeBase)getInnerType()).isBasePrimitive();
			} else {
				return false;
			}
		}
	}

	
}
