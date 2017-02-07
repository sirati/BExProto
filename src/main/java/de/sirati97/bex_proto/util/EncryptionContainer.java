package de.sirati97.bex_proto.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import static de.sirati97.bex_proto.v2.service.modular.internal.BouncyCastleHelper.ASYMMETRIC_TYPE;
import static de.sirati97.bex_proto.v2.service.modular.internal.BouncyCastleHelper.PROVIDER;
import static de.sirati97.bex_proto.v2.service.modular.internal.BouncyCastleHelper.initBouncyCastle;

public class EncryptionContainer implements IEncryptionContainer{
	private KeyStore keyStore;
	private PrivateKey privateKey;
	private PublicKey publicKey;
	private Set<Certificate> certificates = new HashSet<>();
	private KeyFactory keyFactory;
	
	public EncryptionContainer(File certificateFile, String keyStorePass, String keyAlias, String keyPass) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, FileNotFoundException, IOException, UnrecoverableKeyException, KeyManagementException, NoSuchProviderException {
		initBouncyCastle();
		keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
	    keyStore.load(new FileInputStream(certificateFile), keyStorePass.toCharArray());
		Enumeration<String> aliases = keyStore.aliases();
		while (aliases.hasMoreElements()) {
			certificates.add(keyStore.getCertificate(aliases.nextElement()));
		}
	    privateKey = (PrivateKey)keyStore.getKey(keyAlias, keyPass.toCharArray());
	    publicKey = keyStore.getCertificate(keyAlias).getPublicKey();
	    keyFactory = KeyFactory.getInstance(ASYMMETRIC_TYPE, PROVIDER);
	}
	
	public PrivateKey getPrivateKey() {
		return privateKey;
	}
	
	public String getAlgorithm() {
		return getPrivateKey().getAlgorithm();
	}
	
	public PublicKey getPublicKey() {
		return publicKey;
	}
	
	public PublicKey recoverFromData(byte[] keyBytes) throws InvalidKeySpecException {
		return keyFactory.generatePublic(new X509EncodedKeySpec(keyBytes));
	}
	
	public boolean trust(PublicKey key) {
		for (Certificate certificate:certificates) {
			try {
				certificate.verify(key);
				return true;
			} catch (Exception e) {}
		}
		return false;
	}
	
	public String getAlias(PublicKey key) {
		for (Certificate certificate:certificates) {
			try {
				certificate.verify(key);
				return keyStore.getCertificateAlias(certificate);
			} catch (Exception e) {}
		}
		return "";
	}
	
	
}
