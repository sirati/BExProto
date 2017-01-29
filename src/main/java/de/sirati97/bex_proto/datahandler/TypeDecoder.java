package de.sirati97.bex_proto.datahandler;

import de.sirati97.bex_proto.util.CursorByteBuffer;

public class TypeDecoder implements IDecoder<IType> {
	@Override
	public IType decode(CursorByteBuffer dat) {
		boolean isDerived = (Boolean) Type.Boolean.getDecoder().decode(dat);
		if (isDerived) {
			String baseTypeName = (String) Type.String_US_ASCII.getDecoder().decode(dat);
			ArrayType arrayType = new ArrayType(Type.Byte);
			byte[] derivedIds = (byte[]) arrayType.toPrimitiveArray(arrayType.getDecoder().decode(dat));
			
			IType result = Type.get(baseTypeName);
			for (byte id:derivedIds) {
				result = IDerivedType.Register.get(id).create(result);
			}
			return result;
			
		} else {
			String baseTypeName = (String) Type.String_US_ASCII.getDecoder().decode(dat);
			return Type.get(baseTypeName);
			
		}
	}

}
