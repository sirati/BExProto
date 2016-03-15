package de.sirati97.bex_proto.v1.network.adv;

import de.sirati97.bex_proto.util.ByteBuffer;
import de.sirati97.bex_proto.DataHandler.StreamExtractor;
import de.sirati97.bex_proto.DataHandler.Type;

public class SSCWrapperExtractor implements StreamExtractor<SSCWrapper> {

	@Override
	public SSCWrapper extract(ByteBuffer dat) {
		String clientName = (String) Type.String_US_ASCII.getExtractor().extract(dat);
		boolean generic = (Boolean) Type.Boolean.getExtractor().extract(dat);
		int id = (Integer) Type.Integer.getExtractor().extract(dat);
		return new SSCWrapper(clientName, generic, id);
	}

}
