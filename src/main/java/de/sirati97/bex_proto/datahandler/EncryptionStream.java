package de.sirati97.bex_proto.datahandler;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;

public class EncryptionStream implements Stream {
	private final Stream stream;
	private final Cipher cipher;
	
	public EncryptionStream(Stream stream, Cipher cipher) {
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
