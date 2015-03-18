package de.sirati97.bex_proto;


public class ArrayExtractor implements StreamExtractor<Object[]> {
	private TypeBase type;
	
	public ArrayExtractor(TypeBase type) {
		this.type = type;
	}
	
	@Override
	public Object[] extract(ExtractorDat dat) {
		int lenght = (Integer) Type.Integer.getExtractor().extract(dat);
		Object[] result = (Object[]) type.createArray(lenght);
		for (int i=0;i<lenght;i++) {
			result[i] = type.getExtractor().extract(dat);
		}
		return result;
	}

}
