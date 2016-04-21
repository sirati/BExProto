package de.sirati97.bex_proto.datahandler;

import de.sirati97.bex_proto.util.bytebuffer.ByteBuffer;

/**
 * Created by sirati97 on 15.04.2016.
 */
public class ByteArrayStream implements Stream {
    private final byte[] bytes;

    public ByteArrayStream(byte[] bytes) {
        this.bytes = bytes;
    }

    @Override
    public ByteBuffer getByteBuffer() {
        return BExStatic.setByteArray(bytes);
    }
}
