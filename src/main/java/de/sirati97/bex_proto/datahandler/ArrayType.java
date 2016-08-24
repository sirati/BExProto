package de.sirati97.bex_proto.datahandler;

import java.lang.reflect.Array;


public class ArrayType<T> extends DerivedType<T[],T> implements IArrayType<T>{
	private TypeBase<T> type;
	private StreamExtractor<T[]> extractor;
	private Class<T[]> clazz;

	public ArrayType(TypeBase<T> type) {
		this(DerivedTypeBase.Register.ARRAY_TYPE_FACTORY, type);
	}
	
	public ArrayType(ArrayTypeFactory factory, TypeBase<T> type) {
		super(factory);
		this.type = type;
		this.extractor = createExtractor();
		this.clazz = (Class<T[]>) type.createArray(0).getClass();
	}
    protected StreamExtractor<T[]> createExtractor() {
        return new ArrayExtractor<>(type);
    }
	
	@Override
	public boolean isArray() {
		return true;
	}

	@Override
	public TypeBase<T> getInnerType() {
		return type;
	}

	@Override
	public Stream createStream(Object obj) {
		return new ArrayStream(type, obj);
	}

    @Override
    public boolean isEncodable(Object obj, boolean platformIndependent) {
        return clazz.isInstance(obj);
    }

    @Override
    public boolean isEncodable(Class paramClass, boolean platformIndependent) {
        return this.clazz.equals(paramClass);
    }

    @Override
	public StreamExtractor<T[]> getExtractor() {
		return extractor;
	}

	@Override
	public boolean isPrimitive() {return false;}
	
	@Override
	public Object toPrimitiveArray(Object obj) {
		if (type instanceof PrimitiveType) {
			return ((PrimitiveType) type).toPrimitiveArray(obj);
		} else if (type instanceof IArrayType) {
			Object[] obj2 = (Object[]) obj;
			TypeBase base = getBase();
			int dimensions = getDimensions();
			int[] dimArray = new int[dimensions];
			dimArray[0] = obj2.length;
			Object[] temp = (Object[]) Array.newInstance(base.getType(), dimArray);//new Object[obj2.length];
			for (int i=0;i<obj2.length;i++) {
				temp[i] = ((IArrayType) type).toPrimitiveArray(obj2[i]);
			}
			return temp;
		}
		return obj;
	}

	@Override
	protected IArrayType<T[]> createArrayType() {
		return new ArrayType<>(this);
	}

	@Override
	protected INullableType<T[]> createNullableType() {
		return new NullableType<>(this);
	}

	@Override
	public TypeBase<?> getBase() {
		 if (type instanceof IArrayType) {
			 return ((IArrayType) type).getBase();
		 } else {
			 return type;
		 }
	}
	
	@Override
	public int getDimensions() {
		 if (type instanceof IArrayType) {
			 return ((IArrayType) type).getDimensions()+1;
		 } else {
			 return 1;
		 }
	}

	@Override
	public Class<T[]> getType() {
		return clazz;
	}
	
	@Override
	public boolean equals(Object arg0) {
		if (arg0 instanceof IArrayType) {
			return getInnerType().equals(((IArrayType)arg0).getInnerType());
		}
		return super.equals(arg0);
	}

	@Override
	public String getTypeName() {
		return "Array<"+getInnerType().getTypeName()+">";
	}

	@Override
	public IArrayType getInnerArray() {
		return this;
	}

	@Override
	public boolean isBasePrimitive() {
		if (getInnerType() instanceof DerivedTypeBase) {
			return ((DerivedTypeBase)getInnerType()).isBasePrimitive();
		} else {
			return getInnerType().isPrimitive();
		}
	}

	
	
}
