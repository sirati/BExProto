package de.sirati97.bex_proto;


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

	public TypeBase getType() {
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
			return ((ArrayType) type).toPremitiveArray(obj);
		}
		return obj;
	}

}
