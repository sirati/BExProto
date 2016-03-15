package de.sirati97.bex_proto.datahandler;

import java.lang.reflect.Array;


public final class ArrayType extends DerivedType{
	private TypeBase type;
	private StreamExtractor<? extends Object> extractor;
	

	public ArrayType(TypeBase type) {
		this(DerivedTypeBase.Register.ARRAY_TYPE_FACTORY, type);
	}
	
	public ArrayType(ArrayTypeFactory factory, TypeBase type) {
		super(factory);
		this.type = type;
		this.extractor = new ArrayExtractor(type);
	}
	
	@Override
	public boolean isArray() {
		return true;
	}

	public TypeBase getInnerType() {
		return type;
	}
	
	@Override
	public Stream createStream(Object obj) {
		return new ArrayStream(type, obj);
	}

	@Override
	public StreamExtractor<? extends Object> getExtractor() {
		return extractor;
	}

	@Override
	public Object[] createArray(int lenght) {
		return new Object[lenght][];
	}

	@Override
	public boolean isPremitive() {return false;}
	
	@Override
	public Object toPremitiveArray(Object obj) {
		if (type instanceof PremitivType) {
			return ((PremitivType) type).toPremitiveArray(obj);
		} else if (type instanceof ArrayType) {
			Object[] obj2 = (Object[]) obj;
			TypeBase base = getBase();
			int dimensions = getDimensions();
			int[] dimArray = new int[dimensions];
			dimArray[0] = obj2.length;
			Object[] temp = (Object[]) Array.newInstance(base.getType(), dimArray);//new Object[obj2.length];
			for (int i=0;i<obj2.length;i++) {
				temp[i] = ((ArrayType) type).toPremitiveArray(obj2[i]);
			}
			return temp;
		}
		return obj;
	}

	public TypeBase getBase() {
		 if (type instanceof ArrayType) {
			 return ((ArrayType) type).getBase();
		 } else {
			 return type;
		 }
	}
	

	public int getDimensions() {
		 if (type instanceof ArrayType) {
			 return ((ArrayType) type).getDimensions()+1;
		 } else {
			 return 1;
		 }
	}

	@Override
	public Class<?> getType() {
		return null;
	}
	
	@Override
	public boolean equals(Object arg0) {
		if (arg0 instanceof ArrayType) {
			return getInnerType().equals(((ArrayType)arg0).getInnerType());
		}
		return super.equals(arg0);
	}

	@Override
	public String getTypeName() {
		return "Array<"+getInnerType().getTypeName()+">";
	}

	@Override
	public ArrayType getInnerArray() {
		return this;
	}

	@Override
	public boolean isBasePremitive() {
		if (getInnerType() instanceof DerivedTypeBase) {
			return ((DerivedTypeBase)getInnerType()).isBasePremitive();
		} else {
			return getInnerType().isPremitive();
		}
	}

	
	
}
