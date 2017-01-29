package de.sirati97.bex_proto.v1.network.adv;

import de.sirati97.bex_proto.datahandler.EncoderBase;
import de.sirati97.bex_proto.datahandler.Type;
import de.sirati97.bex_proto.util.bytebuffer.ByteBuffer;

public class SSCWrapperEncoder extends EncoderBase<SSCWrapper> {

    @Override
    public void encode(SSCWrapper data, ByteBuffer buffer) {
        Type.String_US_ASCII.getEncoder().encode(data.getClientName(), buffer);
        Type.Boolean.getEncoder().encode(data.isGeneric(), buffer);
        Type.Integer.getEncoder().encode(data.getId(), buffer);
    }
}
