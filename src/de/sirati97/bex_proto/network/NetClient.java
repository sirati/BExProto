package de.sirati97.bex_proto.network;

import java.io.IOException;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import de.sirati97.bex_proto.StreamReader;

public class NetClient extends NetConnection {
	private String ip;
	private int port;
	private Cipher readCipher;
	private ISocketFactory socketFactory;
	
	public NetClient(AsyncHelper asyncHelper, String ip, int port, StreamReader streamReader, ISocketFactory socketFactory, SecretKey secretKey) {
		super(asyncHelper, null, new NetConnectionManager(), streamReader, null, socketFactory, createCipher(secretKey, Cipher.ENCRYPT_MODE));
		this.ip = ip;
		this.port = port;
		this.socketFactory = socketFactory;
		if (secretKey != null) {
			try {
				this.readCipher = Cipher.getInstance(secretKey.getAlgorithm());
				this.readCipher.init(Cipher.DECRYPT_MODE, secretKey);
				streamReader.setCipher(readCipher);
			} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) {
				throw new IllegalStateException(e);
			}
		}
		
	}
	
	private static Cipher createCipher(SecretKey secretKey, int mode) {
		if (secretKey == null)return null;
		Cipher resultCipher;
		try {
			resultCipher = Cipher.getInstance(secretKey.getAlgorithm());
			resultCipher.init(mode, secretKey);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) {
			throw new IllegalStateException(e);
		}
		return resultCipher;
	}
	
	@Override
	public void start() {
		if (isEnabled()) return;

		try {
			Socket socket = socketFactory.createSocket(ip, port);
			setSocket(socket);
			super.start();
		} catch (IOException e) {
			setEnabled(false);
			throw new IllegalStateException(e);
		}
		
	}

}
