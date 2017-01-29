package de.sirati97.bex_proto.datahandler;
import de.sirati97.bex_proto.util.bytebuffer.ByteBuffer;

import java.security.MessageDigest;

/**
 * Created by sirati97 on 17.04.2016.
 */
public class HashModifier implements IHashingStreamModifier {
    private MessageDigest hashAlgorithm;

    public HashModifier(MessageDigest hashAlgorithm) {
        this.hashAlgorithm = hashAlgorithm;
    }

    @Override
    public void setHashAlgorithm(MessageDigest hashAlgorithm) {
        this.hashAlgorithm = hashAlgorithm;
    }

    @Override
    public MessageDigest getHashAlgorithm() {
        return hashAlgorithm;
    }

    @Override
    public ByteBuffer apply(ByteBuffer buffer) {
        byte[] stream = buffer.getBytes();
        buffer.unseal();
        buffer.insertFirst(hashAlgorithm.digest(stream));
        return buffer;
    }

    @Override
    public byte[] unapply(byte[] stream) {
        byte[] hash = new byte[hashAlgorithm.getDigestLength()];
        System.arraycopy(stream, 0, hash, 0, hash.length);
        byte[] newStream = new byte[stream.length-hash.length];
        System.arraycopy(stream, hash.length, newStream, 0, newStream.length);
        byte[] hash2 = hashAlgorithm.digest(newStream);
        for (int i=0;i<hash.length;i++) {
            if (hash[i]!=hash2[i]) {
                throw new IllegalStateException("Hashes do not match! Connection probably compromised.");
            }
        }
        return newStream;
    }

}
