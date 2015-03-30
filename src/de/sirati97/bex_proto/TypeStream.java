package de.sirati97.bex_proto;

import java.util.ArrayList;
import java.util.List;

public class TypeStream implements Stream {
	private TypeBase data;
	
	public TypeStream(TypeBase data) {
		this.data = data;
	}

	@Override
	public byte[] getBytes() {
		boolean isDerived = (data instanceof DerivedTypeBase);
		if (isDerived) {
			List<DerivedTypeBase> derivedTypesList = new ArrayList<>();
			byte[] derivedTypes;
			TypeBase temp = data;
			while (temp instanceof DerivedTypeBase) {
				DerivedTypeBase derivedType = (DerivedTypeBase) temp;
				temp = derivedType.getInnerType();
				derivedTypesList.add(derivedType);
			}
			derivedTypes = new byte[derivedTypesList.size()];
			for (int i=0;i<derivedTypes.length;i++) {
				derivedTypes[derivedTypes.length-i-1]=derivedTypesList.get(i).getDerivedID();
			}
			Stream isDerivedStream = Type.Boolean.createStream(isDerived);
			Stream baseTypeStream = Type.String_US_ASCII.createStream(temp.getTypeName());
			Stream derivedStream = new ArrayType(Type.Byte).createStream(derivedTypes);
			return new MultiStream(isDerivedStream,baseTypeStream, derivedStream).getBytes();
		} else {
			Stream isDerivedStream = Type.Boolean.createStream(isDerived);
			Stream baseTypeStream = Type.String_US_ASCII.createStream(data.getTypeName());
			return new MultiStream(isDerivedStream,baseTypeStream).getBytes();
			
		}
	}

}
