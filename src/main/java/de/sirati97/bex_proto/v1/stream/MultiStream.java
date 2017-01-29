package de.sirati97.bex_proto.v1.stream;

import de.sirati97.bex_proto.util.bytebuffer.ByteBuffer;

/**
 * Created by sirati97 on 29.01.2017 for BexProto.
 */
public class MultiStream implements Stream {
    private final Object[] streams;

    public MultiStream(Object... streams) {
        this.streams = streams;
    }

    @Override
    public ByteBuffer getByteBuffer() {
        ByteBuffer[] buffers = new ByteBuffer[streams.length];
        for (int i = 0; i < streams.length; i++) {
            if (streams[i] instanceof Stream) {
                buffers[i] = ((Stream)streams[i]).getByteBuffer();
            } else if (streams[i] instanceof ByteBuffer) {
                buffers[i] = (ByteBuffer) streams[i];
            } else {
                throw new IllegalStateException("wrong type: " + streams[i].getClass());
            }
        }
        return ByteBuffer.combine(true, buffers);
    }
}
