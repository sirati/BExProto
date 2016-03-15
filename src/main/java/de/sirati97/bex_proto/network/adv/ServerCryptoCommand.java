package de.sirati97.bex_proto.network.adv;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;

import de.sirati97.bex_proto.network.NetConnection;

public class ServerCryptoCommand extends CryptoCommand {
    private KeyGenerator keyGenerator;
	private SecureRandom secureRandom;
    
	public ServerCryptoCommand() {

		try {
			keyGenerator = KeyGenerator.getInstance("AES");
		    secureRandom = SecureRandom.getInstance("SHA1PRNG");
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException(e);
		}
	    keyGenerator.init(128);
	}
	
	@Override
	protected void onCancelHandshake(NetConnection sender) {
		System.out.println("Cancelled");
		sender.stop();
	}
	
	@Override
	protected void onError(String message, NetConnection sender) {
		System.out.println("Client closed encryption handshake with this reason/error: " + message);
		sender.stop();
	}
	
	@Override
	protected void onReceivedPublicKey(byte[] data, NetConnection sender) {
		AdvServer server = (AdvServer)sender.getCreator();
		AdvConnection advConnection = server.getConnectionManager().getAdvConnection(sender);
		if (advConnection == null) {
			closeWithError("Non encyption handshake was not finished!", sender);
			return;
		}
		PublicKey publicKey;
		try {
			publicKey = server.getCryptoContainer().recoverFromData(data);
		} catch (InvalidKeySpecException e) {
			sendError(e.toString(), sender);
			sender.stop();
			return;
		}
		if (server.getCryptoContainer().trust(publicKey) && server.trust(server.getCryptoContainer().getAlias(publicKey), sender)) {
			CryptoHandshakeData handshakeData = new CryptoHandshakeData();
			advConnection.setCryptoHandshakeData(handshakeData);
			handshakeData.setRemotePublicKey(publicKey);
			send(States.PublicKey, server.getCryptoContainer().getPublicKey().getEncoded(), sender);
		} else {
			closeWithError("Client is not trusted!", sender);
			return;
		}
	}
	
	private void closeWithError(String text, NetConnection con) {
		sendError(text, con);
		con.stop();
	}
	
	@Override
	protected void onRequestSecretKey(NetConnection sender) {
		AdvServer server = (AdvServer)sender.getCreator();
		AdvConnection advConnection = server.getConnectionManager().getAdvConnection(sender);
		if (advConnection == null) {
			closeWithError("Unexpected Error! Connection is not registered.", sender);
			return;
		}
		if (advConnection.getCryptoHandshakeData() == null || advConnection.getCryptoHandshakeData().getRemotePublicKey() == null) {
			closeWithError("Unexpected Error! Remote public key is not set.", sender);
			return;
		}
	    Key secretKey = keyGenerator.generateKey();
	    byte[] iv = secureRandom.generateSeed(16);
	    IvParameterSpec secretVector = new IvParameterSpec(iv);
	    advConnection.getCryptoHandshakeData().setSecretKey(secretKey);
	    advConnection.getCryptoHandshakeData().setSecretVector(secretVector);
	    
	    try {
			Cipher cipherClientPublic = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipherClientPublic.init(Cipher.ENCRYPT_MODE, advConnection.getCryptoHandshakeData().getRemotePublicKey());
			byte[] secretKeyBytes = cipherClientPublic.doFinal(secretKey.getEncoded());
			byte[] ivectorBytes = cipherClientPublic.doFinal(iv);
			send(States.SecretKey, secretKeyBytes, ivectorBytes, sender);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
			e.printStackTrace();
			closeWithError("Unexpected Error! " + e.toString(), sender);
			return;
		} 
	}
	
	@Override
	protected void onReceivedSecretKey(byte[] dataKey, byte[] dataVector, NetConnection sender) {
		AdvServer server = (AdvServer)sender.getCreator();
		AdvConnection advConnection = server.getConnectionManager().getAdvConnection(sender);
		if (advConnection == null) {
			closeWithError("Unexpected Error! Connection is not registered.", sender);
			return;
		}
		if (advConnection.getCryptoHandshakeData() == null || advConnection.getCryptoHandshakeData().getRemotePublicKey() == null || advConnection.getCryptoHandshakeData().getSecretKey() == null || advConnection.getCryptoHandshakeData().getSecretVector() == null) {
			closeWithError("Unexpected Error! Handshake data is corrupted.", sender);
			return;
		}
	    
	    try {
			Cipher cipherServerPrivate = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipherServerPrivate.init(Cipher.DECRYPT_MODE, server.getCryptoContainer().getPrivateKey());
			byte[] secretKeyBytesRecieved = cipherServerPrivate.doFinal(dataKey);
			byte[] ivectorBytesRecieved = cipherServerPrivate.doFinal(dataVector);
			byte[] secretKeyBytesLocal = advConnection.getCryptoHandshakeData().getSecretKey().getEncoded();
			byte[] ivectorBytesLocal = advConnection.getCryptoHandshakeData().getSecretVector().getIV();
			if (secretKeyBytesLocal.length != secretKeyBytesRecieved.length) {
				closeWithError("Received invalid secretkey.", sender);
				return;
			}
			if (ivectorBytesLocal.length != ivectorBytesRecieved.length) {
				closeWithError("Received invalid vector.", sender);
				return;
			}
			for (int i=0;i<secretKeyBytesLocal.length;i++) {
				if (secretKeyBytesLocal[i] != secretKeyBytesRecieved[i]) {
					closeWithError("Received invalid secretkey.", sender);
					return;
				}
			}
			for (int i=0;i<ivectorBytesLocal.length;i++) {
				if (ivectorBytesLocal[i] != ivectorBytesRecieved[i]) {
					closeWithError("Received invalid vector.", sender);
					return;
				}
			}
			Cipher sendCipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
			sendCipher.init(Cipher.ENCRYPT_MODE, advConnection.getCryptoHandshakeData().getSecretKey(), advConnection.getCryptoHandshakeData().getSecretVector());
			Cipher receiveCipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
			receiveCipher.init(Cipher.DECRYPT_MODE, advConnection.getCryptoHandshakeData().getSecretKey(), advConnection.getCryptoHandshakeData().getSecretVector());
			send(States.Sussess, sender);
			sender.setReceiveCipher(receiveCipher);
			sender.setSendCipher(sendCipher);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | InvalidAlgorithmParameterException e) {
			closeWithError("Unexpected Error! " + e.toString(), sender);
			return;
		} 
	}
	
	@Override
	protected void onSussess(NetConnection sender) {
		AdvServer server = (AdvServer)sender.getCreator();
		AdvConnection advConnection = server.getConnectionManager().getAdvConnection(sender);
		if (advConnection == null) {
			closeWithError("Unexpected Error! Connection is not registered.", sender);
			return;
		}
		server.sendHandshakeAccepted(advConnection);
	}

}
