package de.sirati97.bex_proto.datahandler;

import de.sirati97.bex_proto.util.bytebuffer.ByteBuffer;

/**
 * Created by sirati97 on 28.04.2016.
 */
public class BitArrayStream implements Stream {
    private final boolean[] bits;

    public BitArrayStream(boolean[] bits) {
        this.bits = bits;
    }

    @Override
    public ByteBuffer getByteBuffer() {
        int byteLength = bits.length/8+(bits.length%8!=0?1:0);
        ByteBuffer result = BExStatic.setInteger(bits.length);
        byte[] bytes = new byte[byteLength];
        for (int i = 0; i < bits.length; i++) {
            if (bits[i]) {
                bytes[i/8] |= BitArrayType.getBitSetter(i%8);
            }
        }
        result.append(bytes);
        return result;
    }
}
