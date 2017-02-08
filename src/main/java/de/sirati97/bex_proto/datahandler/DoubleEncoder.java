package de.sirati97.bex_proto.datahandler;

import de.sirati97.bex_proto.util.bytebuffer.ByteBuffer;

public class DoubleEncoder extends EncoderBase<Double> {

    @Override
    public void encode(Double data, ByteBuffer buffer, boolean header) {
        BExStatic.setDouble(data, buffer);
    }
}
