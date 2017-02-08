package de.sirati97.bex_proto.datahandler;

import de.sirati97.bex_proto.util.CursorByteBuffer;

/**
 * Created by sirati97 on 08.02.2017 for BexProto.
 */
public abstract class DecoderBase<Type> implements IDecoder<Type> {
    @Override
    public Type decode(CursorByteBuffer dat) {
        return decode(dat, true);
    }
}
