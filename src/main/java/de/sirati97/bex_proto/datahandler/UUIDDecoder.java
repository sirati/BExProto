package de.sirati97.bex_proto.datahandler;

import de.sirati97.bex_proto.util.CursorByteBuffer;

import java.util.UUID;

public class UUIDDecoder implements IDecoder<UUID> {

	@Override
	public UUID decode(CursorByteBuffer dat) {
		long least = Type.Long.getDecoder().decode(dat);
		long most = Type.Long.getDecoder().decode(dat);
		return new UUID(most, least);
	}

}
