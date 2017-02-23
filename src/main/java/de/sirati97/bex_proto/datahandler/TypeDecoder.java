package de.sirati97.bex_proto.datahandler;

import de.sirati97.bex_proto.util.CursorByteBuffer;

public class TypeDecoder extends DecoderBase<IType> {
	@Override
	public IType decode(CursorByteBuffer dat, boolean header) {
		boolean isDerived = Types.Boolean.getDecoder().decode(dat);
		if (isDerived) {
			String baseTypeName = Types.String_US_ASCII.getDecoder().decode(dat);
			IArrayType<Byte> arrayType = Types.Byte.asArray();
			byte[] derivedIds = (byte[]) arrayType.toPrimitiveArray(arrayType.getDecoder().decode(dat));
			
			IType result = Types.get(baseTypeName);
			for (byte id:derivedIds) {
				result = IDerivedType.Register.get(id).create(result);
			}
			return result;
			
		} else {
			String baseTypeName =  Types.String_US_ASCII.getDecoder().decode(dat);
			return Types.get(baseTypeName);
			
		}
	}

}
