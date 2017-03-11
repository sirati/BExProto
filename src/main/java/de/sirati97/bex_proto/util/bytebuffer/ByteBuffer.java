package de.sirati97.bex_proto.util.bytebuffer;

import java.util.Iterator;

/**
 * Created by sirati97 on 17.04.2016.
 */
public class ByteBuffer implements IByteBufferCommonBase, Iterable<IByteBufferSegment> {
    public static final String BYTE_BUFFER_IS_SEALED = "ByteBuffer is sealed";
    private IByteBufferSegment first;
    private IByteBufferSegment last;
    private byte[] result;
    private int length=0;

    public ByteBuffer() {}
    public ByteBuffer(byte[]... byteArrays) {
        for (byte[] bytes:byteArrays) {
            append(bytes);
        }
    }
    public ByteBuffer(IByteBufferCommonBase... buffers) {
        for (IByteBufferCommonBase buffer:buffers) {
            append(buffer);
        }
    }

    private void append(IByteBufferSegment newFirst, IByteBufferSegment newLast, int length) {
        if (isSealed()) {
            throw new IllegalStateException(BYTE_BUFFER_IS_SEALED);
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
        IByteBufferSegment segment = new ByteBufferSegment(bytes);
        append(segment, segment, segment.getLength());
        return this;
    }

    public ByteBuffer append(byte[] bytes, int offset, int length) {
        if (bytes == null || bytes.length == 0)return this;
        IByteBufferSegment segment = new OffsetByteBufferSegment(bytes, offset, length);
        append(segment, segment, segment.getLength());
        return this;
    }

    public ByteBuffer append(IByteBufferCommonBase byteBuffer) {
        if (byteBuffer.isSealed()) {
            append(byteBuffer.getBytes());
        } else {
            append(byteBuffer.getFirst(), byteBuffer.getLast(), byteBuffer.getLength());
        }
        return this;
    }

    private void insertFirst(IByteBufferSegment newFirst, IByteBufferSegment newLast, int length) {
        if (isSealed()) {
            throw new IllegalStateException(BYTE_BUFFER_IS_SEALED);
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
        IByteBufferSegment segment = new ByteBufferSegment(bytes);
        insertFirst(segment, segment,segment.getLength());
        return this;
    }

    public ByteBuffer insertFirst(byte[] bytes, int offset, int length) {
        if (bytes == null || bytes.length == 0)return this;
        IByteBufferSegment segment = new OffsetByteBufferSegment(bytes, offset, length);
        insertFirst(segment, segment, segment.getLength());
        return this;
    }

    public ByteBuffer insertFirst(IByteBufferCommonBase byteBuffer) {
        if (byteBuffer.isSealed()) {
            insertFirst(byteBuffer.getBytes());
        } else {
            insertFirst(byteBuffer.getFirst(), byteBuffer.getLast(),byteBuffer.getLength());
        }
        return this;
    }

    @Override
    public IByteBufferSegment getFirst() {
        return first;
    }

    @Override
    public IByteBufferSegment getLast() {
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
            int pos = 0;
            result = new byte[length];
            for (IByteBufferSegment current:this) {
                System.arraycopy(current.getBytes(),current.getOffset(),result,pos,current.getLength());
                pos += current.getLength();
            }
            first = null;
            last = null;
        }
    }

    public void unseal() {
        if (result != null) {
            IByteBufferSegment segment = new ByteBufferSegment(result);
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

    @Override
    public Iterator<IByteBufferSegment> iterator() {
        return new Iterator<IByteBufferSegment>() {
            IByteBufferSegment current = first;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public IByteBufferSegment next() {
               try {
                   return current;
               } finally {
                   current = current.getNext();
               }
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
}
