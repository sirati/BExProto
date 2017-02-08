package de.sirati97.bex_proto.datahandler;

import de.sirati97.bex_proto.util.bytebuffer.ByteBuffer;

import java.util.UUID;

public class UUIDEncoder extends EncoderBase<UUID> {

    @Override
    public void encode(UUID data, ByteBuffer buffer, boolean header) {
        BExStatic.setLong(data.getLeastSignificantBits(), buffer);
        BExStatic.setLong(data.getMostSignificantBits(), buffer);
    }
}
