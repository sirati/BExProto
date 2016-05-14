package de.sirati97.bex_proto.datahandler;

public abstract class PrimitiveType<WrapperType> extends Type<WrapperType> {
	@Override public boolean isPrimitive() {return true;}
	public abstract Object toPrimitiveArray(Object obj);
	public abstract Object toObjectArray(Object obj);
	public abstract WrapperType castToPrimitive(Object obj);

	@SuppressWarnings("unchecked")
	@Override
	public final Stream createStream(Object obj) {
		return createStreamCasted(obj == null?null:castToPrimitive(obj));
	}
}
