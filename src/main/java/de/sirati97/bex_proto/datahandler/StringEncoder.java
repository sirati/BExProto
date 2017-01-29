package de.sirati97.bex_proto.datahandler;

import de.sirati97.bex_proto.util.bytebuffer.ByteBuffer;

import java.nio.charset.Charset;

public class StringEncoder extends EncoderBase<String> {
    private Charset charset;

    public StringEncoder(Charset charset) {
        this.charset = charset;
    }

    @Override
    public void encode(String data, ByteBuffer buffer) {
        BExStatic.setString(data, charset, buffer);
    }

}
