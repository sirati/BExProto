package de.sirati97.bex_proto.v1.network.adv;

import de.sirati97.bex_proto.datahandler.ObjType;
import de.sirati97.bex_proto.datahandler.Stream;
import de.sirati97.bex_proto.datahandler.StreamExtractor;

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
