package de.sirati97.bex_proto.network.adv;

import java.security.Key;
import java.security.PublicKey;

import javax.crypto.spec.IvParameterSpec;

public class CryptoHandshakeData {
	private PublicKey remotePublicKey;
	private Key secretKey;
	private IvParameterSpec secretVector;
	
	public PublicKey getRemotePublicKey() {
		return remotePublicKey;
	}
	
	public void setRemotePublicKey(PublicKey remotePublicKey) {
		this.remotePublicKey = remotePublicKey;
	}
	
	public Key getSecretKey() {
		return secretKey;
	}
	
	public IvParameterSpec getSecretVector() {
		return secretVector;
	}
	
	public void setSecretKey(Key secretKey) {
		this.secretKey = secretKey;
	}
	
	public void setSecretVector(IvParameterSpec secretVector) {
		this.secretVector = secretVector;
	}
}
