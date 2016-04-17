package de.sirati97.bex_proto.datahandler;

import de.sirati97.bex_proto.util.CursorByteBuffer;

import java.util.UUID;

public class UUIDExtractor implements StreamExtractor<UUID> {

	@Override
	public UUID extract(CursorByteBuffer dat) {
		long least = Type.Long.getExtractor().extract(dat);
		long most = Type.Long.getExtractor().extract(dat);
		return new UUID(most, least);
	}

}
