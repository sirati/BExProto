package de.sirati97.bex_proto.datahandler;

import de.sirati97.bex_proto.util.CursorByteBuffer;

public class DynamicObjDecoder extends DecoderBase<DynamicObj> {

	@Override
	public DynamicObj decode(CursorByteBuffer dat, boolean header) {
		IType type = Types.Type.getDecoder().decode(dat);
		Object value = type.getDecoder().decode(dat);
		if (type.isArray() && type instanceof IDerivedType && ((IDerivedType)type).isBasePrimitive()) {
			value = ((IDerivedType)type).getInnerArray().toPrimitiveArray((value));
		}
		return new DynamicObj(type, value);
	}

}
