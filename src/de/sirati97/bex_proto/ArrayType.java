package de.sirati97.bex_proto;

import java.lang.reflect.Array;


public final class ArrayType implements TypeBase {
	private TypeBase type;
	private StreamExtractor<? extends Object> extractor;
	
	public ArrayType(TypeBase type) {
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
	
}
