package de.sirati97.bex_proto.datahandler;


public interface TypeBase<T> {
	boolean isArray();
	boolean isPrimitive();
    Stream createStream(Object obj);
    boolean isEncodable(Object obj, boolean platformIndependent);
    boolean isEncodable(Class clazz, boolean platformIndependent);
	StreamExtractor<T> getExtractor();
	T[] createArray(int length);
	Class<?> getType(); //can be different for primitive types
    Class<T> getObjType();
	String getTypeName();
	INullableType<T> asNullable();
	IArrayType<T> asArray();
}
