package de.sirati97.bex_proto.command;

import com.google.common.base.Charsets;

import de.sirati97.bex_proto.*;

public class TypeHandler {
	
	private StreamExtractor<?> extractor;
	private TypeBase type;
	
	public TypeHandler(TypeBase type) {
		this.type = type;
		if (Type.String_Utf_8.equals(type)) {
			extractor = new StringExtractor(Charsets.UTF_8);
		} else if (Type.String_Utf_16.equals(type)) {
			extractor = new StringExtractor(Charsets.UTF_16);
		} else if (Type.String_US_ASCII.equals(type)) {
			extractor = new StringExtractor(Charsets.US_ASCII);
		} else if (Type.String_ISO_8859_1.equals(type)) {
			extractor = new StringExtractor(Charsets.ISO_8859_1);
		} else if (Type.Integer.equals(type)) {
			extractor = new IntegerExtractor();
		} else if (Type.Long.equals(type)) {
			extractor = new LongExtractor();
		} else if (Type.Short.equals(type)) {
			extractor = new ShortExtractor();
		} else if (Type.Byte.equals(type)) {
			extractor = new ByteExtractor();
		} else if (Type.Double.equals(type)) {
			extractor = new DoubleExtractor();
		}
		
	}
	

	public Object extract(ExtractorDat dat) {
		return extractor.extract(dat);
	}
	
	public Stream createStream(Object object) {
		if (Type.String_Utf_8.equals(type)) {
			return new StringStream((String) object, Charsets.UTF_8);
		} else if (Type.String_Utf_16.equals(type)) {
			return new StringStream((String) object, Charsets.UTF_16);
		} else if (Type.String_US_ASCII.equals(type)) {
			return new StringStream((String) object, Charsets.US_ASCII);
		} else if (Type.String_ISO_8859_1.equals(type)) {
			return new StringStream((String) object, Charsets.ISO_8859_1);
		} else if (Type.Integer.equals(type)) {
			return new IntegerStream((int) object);
		} else if (Type.Long.equals(type)) {
			return new LongStream((long) object);
		} else if (Type.Short.equals(type)) {
			return new ShortStream((short) object);
		} else if (Type.Byte.equals(type)) {
			return new ByteStream((byte) object);
		} else if (Type.Double.equals(type)) {
			return new DoubleStream((double) object);
		} else {
			return null;
		}
	}
	
	public TypeBase getType() {
		return type;
	}
}
