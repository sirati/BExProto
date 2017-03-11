package de.sirati97.bex_proto.util.bytebuffer;

/**
 * Created by sirati97 on 09.03.2017 for BexProto.
 */
public interface IByteBufferSegment extends IByteBufferCommonBase {
    IByteBufferSegment getNext();

    boolean hasNext();

    void setNext(IByteBufferSegment next);

    int getOffset();
}
