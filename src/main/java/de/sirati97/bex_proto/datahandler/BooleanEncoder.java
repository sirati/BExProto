package de.sirati97.bex_proto.datahandler;

import de.sirati97.bex_proto.util.bytebuffer.ByteBuffer;

public class BooleanEncoder extends EncoderBase<Boolean> {

    @Override
    public void encode(Boolean data, ByteBuffer buffer, boolean header) {
        BExStatic.setBoolean(data, buffer);
    }
}
