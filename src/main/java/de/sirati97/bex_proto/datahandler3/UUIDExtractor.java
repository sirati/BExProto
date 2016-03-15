package de.sirati97.bex_proto.datahandler;

import de.sirati97.bex_proto.util.ByteBuffer;

import java.util.UUID;

public class UUIDExtractor implements StreamExtractor<UUID> {

	@Override
	public UUID extract(ByteBuffer dat) {
		String str = (String) Type.String_US_ASCII.getExtractor().extract(dat);
		return UUID.fromString(str);
	}

}
