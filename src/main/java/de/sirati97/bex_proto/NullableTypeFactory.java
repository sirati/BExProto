package de.sirati97.bex_proto;

import de.sirati97.bex_proto.DerivedTypeBase.DerivedFactory;

public class NullableTypeFactory implements DerivedFactory {

	@Override
	public byte getDerivedID() {
		return 1;
	}

	@Override
	public DerivedTypeBase create(TypeBase inner) {
		return new NullableType(this, inner);
	}

}
