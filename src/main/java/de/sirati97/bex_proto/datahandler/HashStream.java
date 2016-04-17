package de.sirati97.bex_proto.datahandler;
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
    public byte[] getBytes() {
        byte[] stream = this.stream.getBytes();
        return (new MultiStream(new HeadlessByteArrayStream(hashAlgorithm.digest(stream)),new HeadlessByteArrayStream(stream))).getBytes();
    }
}
