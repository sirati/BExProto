package de.sirati97.bex_proto.datahandler;

public class NullableTypeFactory implements IDerivedType.DerivedFactory {

	@Override
	public byte getDerivedID() {
		return 1;
	}

	@Override
	public IDerivedType create(IType inner) {
		return inner.asNullable();
	}

}
