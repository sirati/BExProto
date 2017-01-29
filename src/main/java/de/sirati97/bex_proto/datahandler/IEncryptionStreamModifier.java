package de.sirati97.bex_proto.datahandler;

import javax.crypto.Cipher;

/**
 * Created by sirati97 on 29.01.2017 for BexProto.
 */
public interface IEncryptionStreamModifier extends IStreamModifier {
    Cipher getReceiveCipher();
    Cipher getSendCipher();
    void setReceiveCipher(Cipher receiveCipher);
    void setSendCipher(Cipher sendCipher);
}
