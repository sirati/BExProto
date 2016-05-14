package de.sirati97.bex_proto.datahandler;

public abstract class ObjType<T> extends Type<T> {
	@Override public boolean isPrimitive() {return false;}
	public abstract Class<T> getType();

	@Override
	public final Class<T> getObjType() {
		return getType();
	}


	@SuppressWarnings("unchecked")
	@Override
	public final Stream createStream(Object obj) {
		return createStreamCasted((T) obj);
	}
}
