package de.sirati97.bex_proto.v2.service.modular.internal;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.Security;

/**
 * Created by sirati97 on 16.04.2016.
 */
public final class BouncyCastleHelper {
    private static boolean init = false;
    public final static String ASYMMETRIC_TYPE = "RSA";
    public final static String ASYMMETRIC_PATTERN = ASYMMETRIC_TYPE+"/NONE/OAEPWithSHA256AndMGF1Padding";
    public final static String SYMMETRIC_TYPE = "AES";
    public final static String SYMMETRIC_PATTERN = SYMMETRIC_TYPE+"/CBC/PKCS7PADDING";
    public final static int SYMMETRIC_SIZE = 128;
    public final static int IV_LENGTH = 16;
    public final static String PROVIDER = "BC";
    public final static String HASH_ALGORITHM = "SHA-256";

    public static synchronized void initBouncyCastle() {
        if (init)return;
        Security.addProvider(new BouncyCastleProvider());
    }

}
