package de.sirati97.bex_proto.datahandler;

import de.sirati97.bex_proto.util.bytebuffer.ByteBuffer;

/**
 * Created by sirati97 on 15.04.2016.
 */
public class ByteArrayEncoder extends EncoderBase<byte[]> {

    @Override
    public void encode(byte[] data, ByteBuffer buffer, boolean header) {
        BExStatic.setByteArray(data, buffer);
    }
}
