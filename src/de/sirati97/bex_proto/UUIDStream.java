package de.sirati97.bex_proto;

import java.util.UUID;

public class UUIDStream implements Stream {
	private UUID data;
	
	public UUIDStream(UUID data) {
		this.data = data;
	}

	@Override
	public byte[] getBytes() {
		Stream stream1 = Type.Long.createStream(data.getMostSignificantBits());
		Stream stream2 = Type.Long.createStream(data.getLeastSignificantBits());
		return new MultiStream(stream1,stream2).getBytes();
	}

}
