package de.sirati97.bex_proto.datahandler;

import de.sirati97.bex_proto.util.bytebuffer.ByteBuffer;

public class InetAddressPortEncoder extends EncoderBase<InetAddressPort> {
    @Override
    public void encode(InetAddressPort data, ByteBuffer buffer, boolean header) {
        Type.InetAddress.getEncoder().encode(data.getInetAddress(), buffer);
        Type.Integer.getEncoder().encode(data.getPort(), buffer);
    }
}
