package de.sirati97.bex_proto.datahandler;

import de.sirati97.bex_proto.util.bytebuffer.ByteBuffer;

import java.util.UUID;

public class UUIDStream implements Stream {
	private UUID data;
	
	public UUIDStream(UUID data) {
		this.data = data;
	}

	@Override
	public ByteBuffer getByteBuffer() {
		return ByteBuffer.combine(true, BExStatic.setLong(data.getLeastSignificantBits()),BExStatic.setLong(data.getMostSignificantBits()));
	}

}
