package de.sirati97.bex_proto.datahandler;

import de.sirati97.bex_proto.util.bytebuffer.ByteBuffer;

import java.net.InetAddress;

public class InetAddressEncoder extends EncoderBase<InetAddress> {

    @Override
    public void encode(InetAddress data, ByteBuffer buffer, boolean header) {
        BExStatic.setByteArray(data.getAddress(), buffer);
    }
}
