package de.sirati97.bex_proto.v1.network.adv;

import de.sirati97.bex_proto.DataHandler2.ObjType;
import de.sirati97.bex_proto.DataHandler2.Stream;
import de.sirati97.bex_proto.DataHandler2.StreamExtractor;

public class SSCWrapperType extends ObjType {
	SSCWrapperExtractor extractor = new SSCWrapperExtractor();

	@Override
	public Stream createStream(Object obj) {
		return new SSCWrapperStream((SSCWrapper) obj);
	}

	@Override
	public StreamExtractor<? extends Object> getExtractor() {
		return extractor;
	}

	@Override
	public Object[] createArray(int lenght) {
		return new SSCWrapperType[lenght];
	}

	@Override
	public Class<?> getType() {
		return SSCWrapper.class;
	}

	@Override
	public String getTypeName() {
		return "SSCWrapper";
	}

}
