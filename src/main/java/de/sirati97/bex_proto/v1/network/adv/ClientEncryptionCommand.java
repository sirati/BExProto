package de.sirati97.bex_proto.v1.network.adv;

import de.sirati97.bex_proto.v1.network.NetConnection;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;

public class ClientEncryptionCommand extends EncryptionCommand {

	
	@Override
	protected void onRequest(NetConnection sender) {
		AdvClient client = (AdvClient)sender;
		if (client.offersEncryption()) {
			send(States.PublicKey, client.getCryptContainer().getPublicKey().getEncoded(), sender);
		} else {
			send(States.Cancel, sender);
			client.stop();
			System.out.println("Server requires a encrypted connection!");
		}
	}
	
	@Override
	protected void onError(String message, NetConnection sender) {
		System.out.println("Server closed encryption handshake with this reason/error: " + message);
		sender.stop();
	}
	
	protected void onLocalError(String message, NetConnection sender) {
		System.out.println("Client closed encryption handshake with this reason/local error: " + message);
		sender.stop();
	}
	

	@Override
	protected void onReceivedPublicKey(byte[] data, NetConnection sender) {
		AdvClient client = (AdvClient)sender;
		PublicKey publicKey;
		try {
			publicKey = client.getCryptContainer().recoverFromData(data);
		} catch (InvalidKeySpecException e) {
			onLocalError(e.toString(), sender);
			return;
		}
		if (client.getCryptContainer().trust(publicKey)) {
			CryptoHandshakeData handshakeData = new CryptoHandshakeData();
			client.setCryptoHandshakeData(handshakeData);
			handshakeData.setRemotePublicKey(publicKey);
			send(States.RequestSecretKey, sender);
		} else {
			onLocalError("Server is not trusted!", sender);
			return;
		}
	}
	
	@Override
	protected void onReceivedSecretKey(byte[] dataKey, byte[] dataVector, NetConnection sender) {
		AdvClient client = (AdvClient)sender;
		if (client.getCryptoHandshakeData() == null || client.getCryptoHandshakeData().getRemotePublicKey() == null) {
			onLocalError("Unexpected Error! Remote public key is not set.", sender);
			return;
		}
	    
	    try {
			Cipher cipherClientPrivate = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipherClientPrivate.init(Cipher.DECRYPT_MODE, client.getCryptContainer().getPrivateKey());
			byte[] secretKeyRawBytes = cipherClientPrivate.doFinal(dataKey);
			byte[] ivectorRawBytes = cipherClientPrivate.doFinal(dataVector);
			SecretKey secretKey = new SecretKeySpec(secretKeyRawBytes, "AES");
		    IvParameterSpec secretVector = new IvParameterSpec(ivectorRawBytes);
		    client.getCryptoHandshakeData().setSecretKey(secretKey);
		    client.getCryptoHandshakeData().setSecretVector(secretVector);

			Cipher cipherServerPublic = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipherServerPublic.init(Cipher.ENCRYPT_MODE, client.getCryptoHandshakeData().getRemotePublicKey());
			byte[] secretKeyBytes = cipherServerPublic.doFinal(secretKeyRawBytes);
			byte[] ivectorBytes = cipherServerPublic.doFinal(ivectorRawBytes);
			send(States.SecretKey, secretKeyBytes, ivectorBytes, sender);
			
			
			
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
			onLocalError("Unexpected Error! " + e.toString(), sender);
			return;
		} 
	}
	
	@Override
	protected void onSuccess(NetConnection sender) {
		AdvClient client = (AdvClient)sender;
		if (client.getCryptoHandshakeData() == null || client.getCryptoHandshakeData().getRemotePublicKey() == null || client.getCryptoHandshakeData().getSecretKey() == null || client.getCryptoHandshakeData().getSecretVector() == null) {
			onLocalError("Unexpected Error! Handshake data is corrupted.", sender);
			return;
		}
		try {
			Cipher sendCipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
			sendCipher.init(Cipher.ENCRYPT_MODE, client.getCryptoHandshakeData().getSecretKey(), client.getCryptoHandshakeData().getSecretVector());
			Cipher receiveCipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
			receiveCipher.init(Cipher.DECRYPT_MODE, client.getCryptoHandshakeData().getSecretKey(), client.getCryptoHandshakeData().getSecretVector());
			sender.setReceiveCipher(receiveCipher);
			sender.setSendCipher(sendCipher);
			send(States.Success, sender);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException e) {
			onLocalError("Unexpected Error! " + e.toString(), sender);
			return;
		} 
	}
}
