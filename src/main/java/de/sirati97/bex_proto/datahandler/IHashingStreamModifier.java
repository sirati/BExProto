package de.sirati97.bex_proto.datahandler;

import java.security.MessageDigest;

/**
 * Created by sirati97 on 29.01.2017 for BexProto.
 */
public interface IHashingStreamModifier extends IStreamModifier {
    MessageDigest getHashAlgorithm();
    void setHashAlgorithm(MessageDigest hashAlgorithm);
}
