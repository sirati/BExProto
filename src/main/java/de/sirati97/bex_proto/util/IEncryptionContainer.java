package de.sirati97.bex_proto.util;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;

public interface IEncryptionContainer {
	public PrivateKey getPrivateKey();
	public String getAlgorithm();
	public PublicKey getPublicKey();
	public PublicKey recoverFromData(byte[] keyBytes) throws InvalidKeySpecException;
	public boolean trust(PublicKey key);
	public String getAlias(PublicKey key);
}
