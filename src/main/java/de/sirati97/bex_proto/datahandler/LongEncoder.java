package de.sirati97.bex_proto.datahandler;

import de.sirati97.bex_proto.util.bytebuffer.ByteBuffer;

public class LongEncoder extends EncoderBase<Long> {

    @Override
    public void encode(Long data, ByteBuffer buffer) {
        BExStatic.setLong(data, buffer);
    }
}
