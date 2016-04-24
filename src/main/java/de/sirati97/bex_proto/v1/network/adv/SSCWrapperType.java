package de.sirati97.bex_proto.v1.network.adv;

import de.sirati97.bex_proto.datahandler.ObjType;
import de.sirati97.bex_proto.datahandler.Stream;
import de.sirati97.bex_proto.datahandler.StreamExtractor;

public class SSCWrapperType extends ObjType<SSCWrapper> {
	SSCWrapperExtractor extractor = new SSCWrapperExtractor();

	@Override
	public Stream createStreamCasted(SSCWrapper obj) {
		return new SSCWrapperStream(obj);
	}

	@Override
	public StreamExtractor<SSCWrapper> getExtractor() {
		return extractor;
	}

	@Override
	public Class<SSCWrapper> getType() {
		return SSCWrapper.class;
	}

	@Override
	public String getTypeName() {
		return "SSCWrapper";
	}

}
