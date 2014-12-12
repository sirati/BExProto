package de.sirati97.bex_proto;

public class ExtractorDat {
	byte[] bytes;
	int location = 0;

	public ExtractorDat(byte[] bytes) {
		this.bytes = bytes;
	}

	public byte getOne() {
		return bytes[location++];
//		try {
//			
//		} catch (ArrayIndexOutOfBoundsException e) {
//			
//		}
	}
	
	
	public byte[] getMulti(int length) {
		byte[] result = new byte[length];
		System.arraycopy(bytes, location, result, 0, length);
		location += length;
		return result;
	}
	
}
