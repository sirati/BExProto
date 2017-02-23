package de.sirati97.bex_proto.datahandler;

import de.sirati97.bex_proto.util.CursorByteBuffer;

import java.util.UUID;

public class UUIDDecoder extends DecoderBase<UUID> {

	@Override
	public UUID decode(CursorByteBuffer dat, boolean header) {
		long least = Types.Long.getDecoder().decode(dat);
		long most = Types.Long.getDecoder().decode(dat);
		return new UUID(most, least);
	}

}
