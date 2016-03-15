package de.sirati97.bex_proto.DataHandler2;


public interface TypeBase {
	boolean isArray();
	boolean isPremitive();
	Stream createStream(Object obj);
	StreamExtractor<? extends Object> getExtractor();
	Object[] createArray(int lenght);
	Class<?> getType();
	public String getTypeName();
}
