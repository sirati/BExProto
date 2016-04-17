package de.sirati97.bex_proto.util.bytebuffer;

/**
 * Created by sirati97 on 17.04.2016.
 */
public class OffsetByteBufferSegment extends ByteBufferSegment {
    private final int offset;
    private final int length;

    public OffsetByteBufferSegment(byte[] bytes, int offset, int length) {
        super(bytes);
        this.offset = offset;
        this.length = length;
        if (bytes.length-offset<length) {
            throw new IllegalStateException("Byte array is too small for specified offset and length");
        }
    }

    @Override
    public int getOffset() {
        return offset;
    }

    @Override
    public int getLength() {
        return length;
    }
}
