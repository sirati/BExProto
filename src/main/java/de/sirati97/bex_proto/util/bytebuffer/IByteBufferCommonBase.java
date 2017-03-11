package de.sirati97.bex_proto.util.bytebuffer;

/**
 * Created by sirati97 on 17.04.2016.
 */
public interface IByteBufferCommonBase {
    IByteBufferSegment getFirst();
    IByteBufferSegment getLast();
    boolean isSealed();
    byte[] getBytes();
    int getLength();
}
