package de.sirati97.bex_proto;


public class ArrayStream implements Stream{
	private Object[] data;
	private TypeBase type;
	
	public ArrayStream(TypeBase type, Object[] data) {
		this.type = type;
		this.data = data;
	}
	
	
	@Override
	public byte[] getBytes() {
		byte[][] bytess = new byte[data.length][];
		for (int i=0;i<data.length;i++) {
			bytess[i] = type.createStream(data[i]).getBytes();
		}
		byte[] mergedBytes = BExStatic.mergeStream(bytess);
		byte[] lenghtBytes = BExStatic.setInteger(mergedBytes.length);
		byte[] result = new byte[lenghtBytes.length + mergedBytes.length];
		System.arraycopy(lenghtBytes, 0, result, 0, lenghtBytes.length);
		System.arraycopy(mergedBytes, 0, result, lenghtBytes.length, mergedBytes.length);
		return result;
	}

}
