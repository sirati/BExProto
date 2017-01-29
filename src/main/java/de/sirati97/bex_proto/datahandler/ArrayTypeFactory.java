package de.sirati97.bex_proto.datahandler;

public class ArrayTypeFactory implements IDerivedType.DerivedFactory {

	@Override
	public byte getDerivedID() {
		return 0;
	}

	@Override
	public IDerivedType create(IType inner) {
		return new ArrayType(this, inner);
	}

}
