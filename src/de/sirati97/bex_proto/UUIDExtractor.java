package de.sirati97.bex_proto;

import java.util.UUID;

public class UUIDExtractor implements StreamExtractor<UUID> {

	@Override
	public UUID extract(ExtractorDat dat) {
		long most = (Long) Type.Long.getExtractor().extract(dat);
		long least = (Long) Type.Long.getExtractor().extract(dat);
		return new UUID(most, least);
	}

}
