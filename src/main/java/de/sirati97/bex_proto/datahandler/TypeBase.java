package de.sirati97.bex_proto.datahandler;


public interface TypeBase<T> {
	boolean isArray();
	boolean isPrimitive();
	Stream createStream(Object obj);
	StreamExtractor<T> getExtractor();
	T[] createArray(int length);
	Class<?> getType(); //can be different fpr primitive types
	String getTypeName();
}
