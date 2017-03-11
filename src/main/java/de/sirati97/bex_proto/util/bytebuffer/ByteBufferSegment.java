package de.sirati97.bex_proto.util.bytebuffer;

/**
 * Created by sirati97 on 17.04.2016.
 */
public class ByteBufferSegment implements IByteBufferSegment {
    private final byte[] bytes;
    private IByteBufferSegment next;

    public ByteBufferSegment(byte[] bytes) {
        this.bytes = bytes;
    }

    @Override
    public IByteBufferSegment getNext() {
        return next;
    }

    @Override
    public boolean hasNext() {
        return next!=null;
    }

    @Override
    public void setNext(IByteBufferSegment next) {
        this.next = next;
    }

    @Override
    public IByteBufferSegment getFirst() {
        return this;
    }

    @Override
    public IByteBufferSegment getLast() {
        return this;
    }

    @Override
    public boolean isSealed() {
        return false; //reduces class instantiation
    }

    @Override
    public byte[] getBytes() {
        return bytes;
    }

    @Override
    public int getOffset() {
        return 0;
    }

    @Override
    public int getLength() {
        return bytes.length;
    }
}
