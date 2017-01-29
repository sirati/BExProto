package de.sirati97.bex_proto.datahandler;

import de.sirati97.bex_proto.util.bytebuffer.ByteBuffer;

/**
 * Created by sirati97 on 29.01.2017 for BexProto.
 */
public abstract class EncoderBase<Type> implements IEncoder<Type> {
    @Override
    public ByteBuffer encodeIndependent(Type data) {
        ByteBuffer buffer = new ByteBuffer();
        encode(data, buffer);
        return buffer;
    }

    @Override
    public void encodeObj(Object data, ByteBuffer buffer) {
        encode((Type) data, buffer);
    }
}
