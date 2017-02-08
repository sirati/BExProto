package de.sirati97.bex_proto.datahandler;

import de.sirati97.bex_proto.util.bytebuffer.ByteBuffer;

/**
 * Created by sirati97 on 28.04.2016.
 */
public class BitArrayEncoder extends EncoderBase<Boolean[]> {
    @Override
    public void encode(Boolean[] bits, ByteBuffer buffer, boolean header) {
        int byteLength = bits.length/8+(bits.length%8!=0?1:0);
        BExStatic.setInteger(bits.length, buffer);
        byte[] bytes = new byte[byteLength];
        for (int i = 0; i < bits.length; i++) {
            if (bits[i]) {
                bytes[i/8] |= BitArrayType.getBitSetter(i%8);
            }
        }
        buffer.append(bytes);
    }

    @Override
    public void encodeObj(Object data, ByteBuffer buffer, boolean header) {
        encodePrimitive((boolean[]) data, buffer);
    }


    public void encodePrimitive(boolean[] bits, ByteBuffer buffer) {
        int byteLength = bits.length/8+(bits.length%8!=0?1:0);
        BExStatic.setInteger(bits.length, buffer);
        byte[] bytes = new byte[byteLength];
        for (int i = 0; i < bits.length; i++) {
            if (bits[i]) {
                bytes[i/8] |= BitArrayType.getBitSetter(i%8);
            }
        }
        buffer.append(bytes);
    }
}
