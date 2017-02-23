package de.sirati97.bex_proto.datahandler;

import de.sirati97.bex_proto.util.bytebuffer.ByteBuffer;

public class InetAddressPortEncoder extends EncoderBase<InetAddressPort> {
    @Override
    public void encode(InetAddressPort data, ByteBuffer buffer, boolean header) {
        Types.InetAddress.getEncoder().encode(data.getInetAddress(), buffer);
        Types.Integer.getEncoder().encode(data.getPort(), buffer);
    }
}
