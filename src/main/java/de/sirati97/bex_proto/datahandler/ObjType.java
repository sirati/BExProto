package de.sirati97.bex_proto.datahandler;

public abstract class ObjType<T> extends Type<T> {
	@Override public boolean isPrimitive() {return false;}
	public abstract Class<T> getType();
}
