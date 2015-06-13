package de.sirati97.bex_proto;

import java.util.UUID;

public class UUIDExtractor implements StreamExtractor<UUID> {

	@Override
	public UUID extract(ExtractorDat dat) {
		String str = (String) Type.String_US_ASCII.getExtractor().extract(dat);
		return UUID.fromString(str);
	}

}
