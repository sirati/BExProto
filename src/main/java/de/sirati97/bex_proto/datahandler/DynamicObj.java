package de.sirati97.bex_proto.datahandler;

public class DynamicObj {
	private Object value;
	private IType type;
	
	public DynamicObj(IType type, Object value) {
		this.type = type;
		this.value = value;
	}
	
	public Object getValue() {
		return value;
	}
	
	public IType getType() {
		return type;
	}
}
