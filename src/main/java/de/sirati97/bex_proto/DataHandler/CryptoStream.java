package de.sirati97.bex_proto.DataHandler;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;

public class CryptoStream implements Stream {
	private Stream stream;
	private Cipher cipher;
	
	public CryptoStream(Stream stream, Cipher cipher) {
		this.stream = stream;
		this.cipher = cipher;
	}
	
	
	@Override
	public byte[] getBytes() {
		try {
			return cipher.doFinal(stream.getBytes());
		} catch (IllegalBlockSizeException | BadPaddingException e) {
			throw new IllegalStateException(e);
		}
	}

}
