package de.sirati97.bex_proto.util.bytebuffer;

/**
 * Created by sirati97 on 17.04.2016.
 */
public class ByteBufferSegment implements IByteBufferSegment {
    private final byte[] bytes;
    private ByteBufferSegment next;

    public ByteBufferSegment(byte[] bytes) {
        this.bytes = bytes;
    }

    public ByteBufferSegment getNext() {
        if (next == this) {
            System.out.println("OHnononononoo");
        }
        return next;
    }

    public boolean hasNext() {
        return next!=null;
    }

    public void setNext(ByteBufferSegment next) {
        this.next = next;
    }

    @Override
    public ByteBufferSegment getFirst() {
        return this;
    }

    @Override
    public ByteBufferSegment getLast() {
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

    public int getOffset() {
        return 0;
    }

    @Override
    public int getLength() {
        return bytes.length;
    }
}
