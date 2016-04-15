package de.sirati97.bex_proto.datahandler;


public interface TypeBase {
	boolean isArray();
	boolean isPrimitive();
	Stream createStream(Object obj);
	StreamExtractor<? extends Object> getExtractor();
	Object[] createArray(int length);
	Class<?> getType();
	public String getTypeName();
}
