package de.sirati97.bex_proto.datahandler;


import de.sirati97.bex_proto.util.ByteBuffer;

public class ArrayExtractor implements StreamExtractor<Object[]> {
	private TypeBase type;
	
	public ArrayExtractor(TypeBase type) {
		this.type = type;
	}
	
	@Override
	public Object[] extract(ByteBuffer dat) {
		int lenght = (Integer) Type.Integer.getExtractor().extract(dat);
		Object[] result = (Object[]) type.createArray(lenght);
		for (int i=0;i<lenght;i++) {
			result[i] = type.getExtractor().extract(dat);
		}
		return result;
	}

}
