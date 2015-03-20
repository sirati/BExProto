package de.sirati97.bex_proto.network.adv;

import de.sirati97.bex_proto.ExtractorDat;
import de.sirati97.bex_proto.StreamExtractor;
import de.sirati97.bex_proto.Type;

public class SSCWrapperExtractor implements StreamExtractor<SSCWrapper> {

	@Override
	public SSCWrapper extract(ExtractorDat dat) {
		String clientName = (String) Type.String_US_ASCII.getExtractor().extract(dat);
		boolean generic = (Boolean) Type.Boolean.getExtractor().extract(dat);
		int id = (Integer) Type.Integer.getExtractor().extract(dat);
		return new SSCWrapper(clientName, generic, id);
	}

}
