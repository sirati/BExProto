package de.sirati97.bex_proto.network.adv;

import de.sirati97.bex_proto.MultiStream;
import de.sirati97.bex_proto.Stream;
import de.sirati97.bex_proto.Type;

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
