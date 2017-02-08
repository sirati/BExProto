package de.sirati97.bex_proto.datahandler;

import de.sirati97.bex_proto.util.bytebuffer.ByteBuffer;

/**
 * Created by sirati97 on 29.01.2017 for BexProto.
 */
public abstract class EncoderBase<Type> implements IEncoder<Type> {
    @Override
    public ByteBuffer encodeIndependent(Type data) {
        ByteBuffer buffer = new ByteBuffer();
        encode(data, buffer, false);
        return buffer;
    }

    @Override
    public void encodeObj(Object data, ByteBuffer buffer, boolean header) {
        encode((Type) data, buffer, header);
    }

    @Override
    public void encodeObj(Object data, ByteBuffer buffer) {
        encodeObj(data, buffer, false);
    }

    @Override
    public void encode(Type data, ByteBuffer buffer) {
        encode(data, buffer, false);
    }
}
