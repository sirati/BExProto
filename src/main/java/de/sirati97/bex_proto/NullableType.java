package de.sirati97.bex_proto;


public class NullableType extends DerivedType {
	private TypeBase type;
	private StreamExtractor<? extends Object> extractor;


	public NullableType(TypeBase type) {
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
	public boolean isPremitive() {
		return false;
	}

	@Override
	public Stream createStream(Object obj) {
		return new NullableStream(getInnerType(), obj);
	}

	@Override
	public StreamExtractor<? extends Object> getExtractor() {
		return extractor;
	}

	@Override
	public Object[] createArray(int lenght) {
		return getInnerType().createArray(lenght);
	}

	@Override
	public Class<?> getType() {
		return getInnerType().getClass();
	}
	
	public TypeBase getInnerType() {
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
	public boolean isBasePremitive() {
		if (getInnerType().isPremitive()) {
			return false;
		} else {
			if (getInnerType() instanceof DerivedTypeBase) {
				return ((DerivedTypeBase)getInnerType()).isBasePremitive();
			} else {
				return false;
			}
		}
	}

	
}
