package de.sirati97.bex_proto.datahandler;


public interface IType<T> {
	boolean isArray();
	boolean isPrimitive();
    IEncoder<T> getEncoder();
    boolean isEncodable(Object obj, boolean platformIndependent);
    boolean isEncodable(Class clazz, boolean platformIndependent);
	IDecoder<T> getDecoder();
	T[] createArray(int length);
	Class<?> getType(); //can be different for primitive types
    Class<T> getObjType();
	String getTypeName();
	INullableType<T> asNullable();
	IArrayType<T> asArray();
}
