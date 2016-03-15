package de.sirati97.bex_proto.datahandler_;

public class ArrayTypeFactory implements DerivedTypeBase.DerivedFactory {

	@Override
	public byte getDerivedID() {
		return 0;
	}

	@Override
	public DerivedTypeBase create(TypeBase inner) {
		return new ArrayType(this, inner);
	}

}
