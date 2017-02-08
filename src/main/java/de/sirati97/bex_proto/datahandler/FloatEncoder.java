package de.sirati97.bex_proto.datahandler;

import de.sirati97.bex_proto.util.bytebuffer.ByteBuffer;

public class FloatEncoder extends EncoderBase<Float> {

    @Override
    public void encode(Float data, ByteBuffer buffer, boolean header) {
        BExStatic.setFloat(data, buffer);
    }
}
