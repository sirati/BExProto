package de.sirati97.bex_proto.command;

import com.google.common.base.Charsets;

import de.sirati97.bex_proto.ExtractorDat;
import de.sirati97.bex_proto.Stream;
import de.sirati97.bex_proto.StreamExtractor;
import de.sirati97.bex_proto.StringExtractor;
import de.sirati97.bex_proto.StringStream;

public class TypeHandler {
	
	private StreamExtractor<?> extractor;
	private Type type;
	
	public TypeHandler(Type type) {
		this.type = type;
		if (Type.String_Utf_8.equals(type)) {
			extractor = new StringExtractor(Charsets.UTF_8);
		} else if (Type.String_Utf_16.equals(type)) {
			extractor = new StringExtractor(Charsets.UTF_16);
		} else if (Type.String_US_ASCII.equals(type)) {
			extractor = new StringExtractor(Charsets.US_ASCII);
		} else if (Type.String_ISO_8859_1.equals(type)) {
			extractor = new StringExtractor(Charsets.ISO_8859_1);
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
		} else {
			return null;
		}
	}
	
	public Type getType() {
		return type;
	}
}
