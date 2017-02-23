package de.sirati97.bex_proto.v1.network.adv;

import de.sirati97.bex_proto.datahandler.EncoderBase;
import de.sirati97.bex_proto.datahandler.Types;
import de.sirati97.bex_proto.util.bytebuffer.ByteBuffer;

public class SSCWrapperEncoder extends EncoderBase<SSCWrapper> {

    @Override
    public void encode(SSCWrapper data, ByteBuffer buffer, boolean header) {
        Types.String_US_ASCII.getEncoder().encode(data.getClientName(), buffer);
        Types.Boolean.getEncoder().encode(data.isGeneric(), buffer);
        Types.Integer.getEncoder().encode(data.getId(), buffer);
    }
}
