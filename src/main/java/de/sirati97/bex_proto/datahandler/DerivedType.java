package de.sirati97.bex_proto.datahandler;

import java.lang.reflect.Array;

public abstract class DerivedType<Type,InnerType> implements DerivedTypeBase<Type,InnerType> {
	private DerivedFactory factory;
	private INullableType<Type> nullableType;
	private IArrayType<Type> arrayType;
	
	public DerivedType(DerivedFactory factory) {
		this.factory = factory;
	}
	
	public DerivedFactory getFactory() {
		return factory;
	}
	
	@Override
	public byte getDerivedID() {
		return getFactory().getDerivedID();
	}
	
	@Override
	public Object toPrimitiveArray(Object obj) {
		if (isArray()) {
			return getInnerArray().toPrimitiveArray(obj);
		}
		return obj;
	}

	protected abstract IArrayType<Type> createArrayType();
	protected abstract INullableType<Type> createNullableType();

	@Override
	public final INullableType<Type> asNullable() {
		return nullableType==null?(nullableType=createNullableType()):nullableType;
	}

	@Override
	public final IArrayType<Type> asArray() {
		return arrayType==null?(arrayType=createArrayType()):arrayType;
	}

    @Override
    public final Class<Type> getObjType() {
        return getType();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Type[] createArray(int length) {
        return (Type[]) Array.newInstance(getObjType(), length);
    }

}
