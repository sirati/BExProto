package de.sirati97.bex_proto.datahandler;

import java.lang.reflect.Array;

public abstract class DerivedType<Type,InnerType> extends CommonTypeBase<Type> implements IDerivedType<Type,InnerType> {
	private DerivedFactory factory;

    public DerivedType(IEncoder<Type> encoder, IDecoder<Type> decoder, DerivedFactory factory) {
        super(encoder, decoder);
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
