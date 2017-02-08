package de.sirati97.bex_proto.datahandler;

import de.sirati97.bex_proto.util.bytebuffer.ByteBuffer;

public class ShortEncoder extends EncoderBase<Short> {

    @Override
    public void encode(Short data, ByteBuffer buffer, boolean header) {
        BExStatic.setShort(data, buffer);
    }
}
