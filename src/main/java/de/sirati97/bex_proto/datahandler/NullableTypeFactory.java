package de.sirati97.bex_proto.datahandler;

public class NullableTypeFactory implements DerivedTypeBase.DerivedFactory {

	@Override
	public byte getDerivedID() {
		return 1;
	}

	@Override
	public DerivedTypeBase create(TypeBase inner) {
		return new NullableType(this, inner);
	}

}
