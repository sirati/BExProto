package de.sirati97.bex_proto.datahandler;

import de.sirati97.bex_proto.util.ByteBuffer;

public class DynamicObjExtractor implements StreamExtractor<DynamicObj> {

	@Override
	public DynamicObj extract(ByteBuffer dat) {
		TypeBase type = (TypeBase) Type.Type.getExtractor().extract(dat);
		Object value = type.getExtractor().extract(dat);
		if (type.isArray() && type instanceof DerivedTypeBase && ((DerivedTypeBase)type).isBasePrimitive()) {
			value = ((DerivedTypeBase)type).getInnerArray().toPrimitiveArray((value));
		}
		return new DynamicObj(type, value);
	}

}
