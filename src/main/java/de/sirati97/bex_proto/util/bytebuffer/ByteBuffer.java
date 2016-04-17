package de.sirati97.bex_proto.util.bytebuffer;

/**
 * Created by sirati97 on 17.04.2016.
 */
public class ByteBuffer implements IByteBufferSegment {
    private ByteBufferSegment first;
    private ByteBufferSegment last;
    private byte[] result;
    private int length=0;

    public ByteBuffer() {}
    public ByteBuffer(byte[]... byteArrays) {
        for (byte[] bytes:byteArrays) {
            append(bytes);
        }
    }
    public ByteBuffer(IByteBufferSegment... buffers) {
        for (IByteBufferSegment buffer:buffers) {
            append(buffer);
        }
    }

    private void append(ByteBufferSegment newFirst, ByteBufferSegment newLast, int length) {
        if (isSealed()) {
            throw new IllegalStateException("ByteBuffer is sealed");
        }
        if (newFirst == null || newLast == null) {
            return;
        }
        if (first == null) {
            first = newFirst;
            last = newLast;
        } else {
            last.setNext(newFirst);
            last = newLast;
        }
        this.length += length;
    }

    public ByteBuffer append(byte[] bytes) {
        if (bytes == null || bytes.length == 0)return this;
        ByteBufferSegment segment = new ByteBufferSegment(bytes);
        append(segment, segment, segment.getLength());
        return this;
    }

    public ByteBuffer append(byte[] bytes, int offset, int length) {
        if (bytes == null || bytes.length == 0)return this;
        ByteBufferSegment segment = new OffsetByteBufferSegment(bytes, offset, length);
        append(segment, segment, segment.getLength());
        return this;
    }

    public ByteBuffer append(IByteBufferSegment byteBuffer) {
        if (byteBuffer.isSealed()) {
            append(byteBuffer.getBytes());
        } else {
            append(byteBuffer.getFirst(), byteBuffer.getLast(), byteBuffer.getLength());
        }
        return this;
    }

    private void insertFirst(ByteBufferSegment newFirst, ByteBufferSegment newLast, int length) {
        if (isSealed()) {
            throw new IllegalStateException("ByteBuffer is sealed");
        }
        if (newFirst == null || newLast == null) {
            return;
        }
        if (first == null) {
            first = newFirst;
            last = newLast;
        } else {
            newLast.setNext(first);
            first = newFirst;
        }
        this.length += length;
    }

    public ByteBuffer insertFirst(byte[] bytes) {
        if (bytes == null || bytes.length == 0)return this;
        ByteBufferSegment segment = new ByteBufferSegment(bytes);
        insertFirst(segment, segment,segment.getLength());
        return this;
    }

    public ByteBuffer insertFirst(byte[] bytes, int offset, int length) {
        if (bytes == null || bytes.length == 0)return this;
        ByteBufferSegment segment = new OffsetByteBufferSegment(bytes, offset, length);
        insertFirst(segment, segment, segment.getLength());
        return this;
    }

    public ByteBuffer insertFirst(IByteBufferSegment byteBuffer) {
        if (byteBuffer.isSealed()) {
            insertFirst(byteBuffer.getBytes());
        } else {
            insertFirst(byteBuffer.getFirst(), byteBuffer.getLast(),byteBuffer.getLength());
        }
        return this;
    }

    @Override
    public ByteBufferSegment getFirst() {
        return first;
    }

    @Override
    public ByteBufferSegment getLast() {
        return last;
    }

    public boolean isSealed() {
        return result != null;
    }

    public byte[] getBytes() {
        seal();
        return result;
    }

    public void seal() {
        if (result == null) {
            int length = 0;
            ByteBufferSegment current = first;
            while (current != null) {
                length += current.getLength();
                current = current.getNext();
            }
            current = first;
            int pos = 0;
            result = new byte[length];
            while (current != null) {
                System.arraycopy(current.getBytes(),current.getOffset(),result,pos,current.getLength());
                pos += current.getLength();
                current = current.getNext();
            }
            first = null;
            last = null;
        }
    }

    public void unseal() {
        if (result != null) {
            ByteBufferSegment segment = new ByteBufferSegment(result);
            result = null;
            first = segment;
            last = segment;
        }
    }

    @Override
    public int getLength() {
        return length;
    }

    public static ByteBuffer combine(boolean reuseBuffers, ByteBuffer... buffers) {
        if (buffers.length == 0)return new ByteBuffer();
        ByteBuffer result = reuseBuffers?buffers[0]:new ByteBuffer();
        result.unseal();
        for (int i=reuseBuffers?1:0;i<buffers.length;i++) {
            result.append(buffers[i]);
        }
        return result;
    }
}
