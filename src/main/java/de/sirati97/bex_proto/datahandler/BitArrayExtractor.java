package de.sirati97.bex_proto.datahandler;

import de.sirati97.bex_proto.util.CursorByteBuffer;

/**
 * Created by sirati97 on 28.04.2016.
 */
public class BitArrayExtractor implements StreamExtractor<Boolean[]> {
    @Override
    public Boolean[] extract(CursorByteBuffer dat) {
        int bitsLength = BExStatic.getInteger(dat.getMulti(4));
        Boolean[] result = new Boolean[bitsLength];
        byte current=0;
        for (int i = 0; i < bitsLength; i++) {
            if (i%8==0) {
                current = dat.getOne();
            }
            result[i] = (current & BitArrayType.getBitSetter(i%8)) > 0;
        }
        return result;
    }
}
