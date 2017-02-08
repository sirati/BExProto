package de.sirati97.bex_proto.datahandler;

import de.sirati97.bex_proto.util.bytebuffer.ByteBuffer;

public class IntegerEncoder extends EncoderBase<Integer> {

    @Override
    public void encode(Integer data, ByteBuffer buffer, boolean header) {
        BExStatic.setInteger(data, buffer);
    }
}
