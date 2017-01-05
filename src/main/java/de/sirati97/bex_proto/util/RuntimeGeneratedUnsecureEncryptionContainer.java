package de.sirati97.bex_proto.util;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import static de.sirati97.bex_proto.v2.modular.internal.BouncyCastleHelper.ASYMMETRIC_TYPE;
import static de.sirati97.bex_proto.v2.modular.internal.BouncyCastleHelper.PROVIDER;
import static de.sirati97.bex_proto.v2.modular.internal.BouncyCastleHelper.initBouncyCastle;

/**
 * Created by sirati97 on 15.04.2016.
 */
public class RuntimeGeneratedUnsecureEncryptionContainer implements IEncryptionContainer {
    private final KeyPair keyPair;
    private final KeyFactory keyFactory;

    public RuntimeGeneratedUnsecureEncryptionContainer(){
        initBouncyCastle();
        KeyPairGenerator kpg = null;
        try {
            kpg = KeyPairGenerator.getInstance(ASYMMETRIC_TYPE, PROVIDER);
            kpg.initialize(1024);
            keyPair = kpg.generateKeyPair();
            keyFactory = KeyFactory.getInstance(ASYMMETRIC_TYPE, PROVIDER);
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public PrivateKey getPrivateKey() {
        return keyPair.getPrivate();
    }

    @Override
    public String getAlgorithm() {
        return keyPair.getPublic().getAlgorithm();
    }

    @Override
    public PublicKey getPublicKey() {
        return keyPair.getPublic();
    }

    @Override
    public PublicKey recoverFromData(byte[] keyBytes) throws InvalidKeySpecException {
        return keyFactory.generatePublic(new X509EncodedKeySpec(keyBytes));
    }

    @Override
    public boolean trust(PublicKey key) {
        return true;
    }

    @Override
    public String getAlias(PublicKey key) {
        return key.equals(getPublicKey())?"Own":"Unknown";
    }
}
