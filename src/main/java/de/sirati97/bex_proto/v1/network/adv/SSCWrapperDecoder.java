package de.sirati97.bex_proto.v1.network.adv;

import de.sirati97.bex_proto.util.CursorByteBuffer;
import de.sirati97.bex_proto.datahandler.IDecoder;
import de.sirati97.bex_proto.datahandler.Type;

public class SSCWrapperDecoder implements IDecoder<SSCWrapper> {

	@Override
	public SSCWrapper decode(CursorByteBuffer dat) {
		String clientName = (String) Type.String_US_ASCII.getDecoder().decode(dat);
		boolean generic = (Boolean) Type.Boolean.getDecoder().decode(dat);
		int id = (Integer) Type.Integer.getDecoder().decode(dat);
		return new SSCWrapper(clientName, generic, id);
	}

}
