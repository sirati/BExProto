package de.sirati97.bex_proto.DataHandler2;

import java.util.UUID;

public class UUIDStream implements Stream {
	private UUID data;
	
	public UUIDStream(UUID data) {
		this.data = data;
	}

	@Override
	public byte[] getBytes() {
		Stream stream = Type.String_US_ASCII.createStream(data.toString());
		return stream.getBytes();
	}

}
