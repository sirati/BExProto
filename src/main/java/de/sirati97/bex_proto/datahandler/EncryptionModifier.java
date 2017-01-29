package de.sirati97.bex_proto.datahandler;

import de.sirati97.bex_proto.util.bytebuffer.ByteBuffer;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;

public class EncryptionModifier implements IEncryptionStreamModifier {
    private Cipher sendCipher;
    private Cipher receiveCipher;

    public EncryptionModifier(Cipher sendCipher, Cipher receiveCipher) {
        this.sendCipher = sendCipher;
        this.receiveCipher = receiveCipher;
    }

    @Override
    public void setReceiveCipher(Cipher receiveCipher) {
        this.receiveCipher = receiveCipher;
    }

    @Override
    public void setSendCipher(Cipher sendCipher) {
        this.sendCipher = sendCipher;
    }

    @Override
    public Cipher getReceiveCipher() {
        return receiveCipher;
    }

    @Override
    public Cipher getSendCipher() {
        return sendCipher;
    }

    @Override
    public ByteBuffer apply(ByteBuffer buffer) {
        try {
            return new ByteBuffer(sendCipher.doFinal(buffer.getBytes()));
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public byte[] unapply(byte[] stream) {
        try {
            return receiveCipher.doFinal(stream);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            throw new IllegalStateException(e);
        }
    }
}
