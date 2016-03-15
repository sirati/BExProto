package de.sirati97.bex_proto.datahandler_;


public class ArrayStream implements Stream{
	private Object dataObj;
	private TypeBase baseType;
	
	public ArrayStream(TypeBase baseType, Object data) {
		this.baseType = baseType;
		if (baseType instanceof PremitivType) {
			data = ((PremitivType) baseType).toObjectArray(data);
		}
		this.dataObj = data;
	}
	
	
	@Override
	public byte[] getBytes() {
		Object[] data = (Object[]) dataObj;
		
		byte[][] bytess = new byte[data.length][];
		for (int i=0;i<data.length;i++) {
			bytess[i] = baseType.createStream(data[i]).getBytes();
		}
		byte[] mergedBytes = BExStatic.mergeStream(bytess);
		byte[] lenghtBytes = BExStatic.setInteger(data.length);
		byte[] result = new byte[lenghtBytes.length + mergedBytes.length];
		System.arraycopy(lenghtBytes, 0, result, 0, lenghtBytes.length);
		System.arraycopy(mergedBytes, 0, result, lenghtBytes.length, mergedBytes.length);
		return result;
	}

}
