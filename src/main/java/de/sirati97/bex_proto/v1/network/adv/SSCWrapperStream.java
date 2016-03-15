package de.sirati97.bex_proto.v1.network.adv;

import de.sirati97.bex_proto.DataHandler2.MultiStream;
import de.sirati97.bex_proto.DataHandler2.Stream;
import de.sirati97.bex_proto.DataHandler2.Type;

public class SSCWrapperStream implements Stream {
	SSCWrapper data;
	
	public SSCWrapperStream(SSCWrapper data) {
		this.data = data;
	}
	
	
	@Override
	public byte[] getBytes() {
		Stream clientName = Type.String_US_ASCII.createStream(data.getClientName());
		Stream generic = Type.Boolean.createStream(data.isGeneric());
		Stream id = Type.Integer.createStream(data.getId());
		return new MultiStream(clientName, generic, id).getBytes();
	}

}
