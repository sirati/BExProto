package de.sirati97.bex_proto.v1.stream;

import de.sirati97.bex_proto.util.bytebuffer.ByteBuffer;

/**
 * Created by sirati97 on 29.01.2017 for BexProto.
 */
public class ByteBufferStream implements Stream {
    private final ByteBuffer buffer;

    public ByteBufferStream(ByteBuffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public ByteBuffer getByteBuffer() {
        return buffer;
    }
}
