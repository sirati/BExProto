package de.sirati97.bex_proto.datahandler;
import de.sirati97.bex_proto.util.bytebuffer.ByteBuffer;
import de.sirati97.bex_proto.util.bytebuffer.ByteBufferSegment;

import java.security.MessageDigest;

/**
 * Created by sirati97 on 17.04.2016.
 */
public class HashStream implements Stream {
    private final Stream stream;
    private final MessageDigest hashAlgorithm;

    public HashStream(Stream stream, MessageDigest hashAlgorithm) {
        this.stream = stream;
        this.hashAlgorithm = hashAlgorithm;
    }


    @Override
    public ByteBuffer getBytes() {
        byte[] stream = this.stream.getBytes().getBytes();
        return new ByteBuffer(new ByteBufferSegment(hashAlgorithm.digest(stream)),new ByteBufferSegment(stream));
    }
}
