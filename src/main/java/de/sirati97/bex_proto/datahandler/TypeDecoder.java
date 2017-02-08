package de.sirati97.bex_proto.datahandler;

import de.sirati97.bex_proto.util.CursorByteBuffer;

public class TypeDecoder extends DecoderBase<IType> {
	@Override
	public IType decode(CursorByteBuffer dat, boolean header) {
		boolean isDerived = Type.Boolean.getDecoder().decode(dat);
		if (isDerived) {
			String baseTypeName = Type.String_US_ASCII.getDecoder().decode(dat);
			IArrayType<Byte> arrayType = Type.Byte.asArray();
			byte[] derivedIds = (byte[]) arrayType.toPrimitiveArray(arrayType.getDecoder().decode(dat));
			
			IType result = Type.get(baseTypeName);
			for (byte id:derivedIds) {
				result = IDerivedType.Register.get(id).create(result);
			}
			return result;
			
		} else {
			String baseTypeName =  Type.String_US_ASCII.getDecoder().decode(dat);
			return Type.get(baseTypeName);
			
		}
	}

}
