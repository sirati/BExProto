package de.sirati97.bex_proto.datahandler;

/**
 * Created by sirati97 on 15.04.2016.
 */
public class ByteArrayStream implements Stream {
    private final byte[] bytes;

    public ByteArrayStream(byte[] bytes) {
        this.bytes = bytes;
    }

    @Override
    public byte[] getBytes() {
        return BExStatic.setByteArray(bytes);
    }
}
