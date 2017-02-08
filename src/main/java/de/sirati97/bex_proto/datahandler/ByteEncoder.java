package de.sirati97.bex_proto.datahandler;

import de.sirati97.bex_proto.util.bytebuffer.ByteBuffer;

public class ByteEncoder extends EncoderBase<Byte> {

    @Override
    public void encode(Byte data, ByteBuffer buffer, boolean header) {
        BExStatic.setByte(data, buffer);
    }
}
