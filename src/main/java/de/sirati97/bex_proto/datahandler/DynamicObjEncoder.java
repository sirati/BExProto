package de.sirati97.bex_proto.datahandler;

import de.sirati97.bex_proto.util.bytebuffer.ByteBuffer;

public class DynamicObjEncoder extends EncoderBase<DynamicObj> {

    @Override
    public void encode(DynamicObj data, ByteBuffer buffer, boolean header) {
        Type.Type.getEncoder().encode(data.getType(), buffer);
        data.getType().getEncoder().encode(data.getValue(), buffer);
    }
}
