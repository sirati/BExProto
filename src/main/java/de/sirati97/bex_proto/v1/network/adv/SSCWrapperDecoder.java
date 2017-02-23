package de.sirati97.bex_proto.v1.network.adv;

import de.sirati97.bex_proto.datahandler.DecoderBase;
import de.sirati97.bex_proto.datahandler.Types;
import de.sirati97.bex_proto.util.CursorByteBuffer;

public class SSCWrapperDecoder extends DecoderBase<SSCWrapper> {

	@Override
	public SSCWrapper decode(CursorByteBuffer dat, boolean header) {
		String clientName = Types.String_US_ASCII.getDecoder().decode(dat);
		boolean generic = Types.Boolean.getDecoder().decode(dat);
		int id = Types.Integer.getDecoder().decode(dat);
		return new SSCWrapper(clientName, generic, id);
	}

}
