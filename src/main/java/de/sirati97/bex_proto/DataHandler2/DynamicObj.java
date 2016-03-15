package de.sirati97.bex_proto.DataHandler2;

public class DynamicObj {
	private Object value;
	private TypeBase type;
	
	public DynamicObj(TypeBase type, Object value) {
		this.type = type;
		this.value = value;
	}
	
	public Object getValue() {
		return value;
	}
	
	public TypeBase getType() {
		return type;
	}
}
