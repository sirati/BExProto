package de.sirati97.bex_proto.datahandler;

public abstract class ObjType<T> extends Type<T> {

    public ObjType(IEncoder<T> encoder, IDecoder<T> decoder) {
        super(encoder, decoder);
    }

    @Override public boolean isPrimitive() {return false;}
	public abstract Class<T> getType();

	@Override
	public final Class<T> getObjType() {
		return getType();
	}

    @Override
    public boolean isEncodable(Class clazz, boolean platformIndependent) {
        return getType().isAssignableFrom(clazz);
    }
}
