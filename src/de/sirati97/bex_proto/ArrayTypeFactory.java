package de.sirati97.bex_proto;

import de.sirati97.bex_proto.DerivedTypeBase.DerivedFactory;

public class ArrayTypeFactory implements DerivedFactory {

	@Override
	public byte getDerivedID() {
		return 0;
	}

	@Override
	public DerivedTypeBase create(TypeBase inner) {
		return new ArrayType(this, inner);
	}

}
