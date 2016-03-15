package de.sirati97.bex_proto.datahandler_;

public abstract class DerivedType implements DerivedTypeBase {
	private DerivedFactory factory;
	
	public DerivedType(DerivedFactory factory) {
		this.factory = factory;
	}
	
	public DerivedFactory getFactory() {
		return factory;
	}
	
	@Override
	public byte getDerivedID() {
		return getFactory().getDerivedID();
	}
	
	@Override
	public Object toPremitiveArray(Object obj) {
		if (isArray()) {
			return getInnerArray().toPremitiveArray(obj);
		}
		return obj;
	}

}
