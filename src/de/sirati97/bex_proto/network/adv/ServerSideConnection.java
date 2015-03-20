package de.sirati97.bex_proto.network.adv;

public class ServerSideConnection {
	private String clientName;
	private boolean generic;
	private int id;
	
	public ServerSideConnection(String clientName, boolean generic, int id) {
		this.clientName = clientName;
		this.generic = generic;
		this.id = id;
	}

	public String getClientName() {
		return clientName;
	}

	public int getId() {
		return id;
	}
	
	protected void setId(int id) {
		this.id = id;
	}

	public boolean isGeneric() {
		return generic;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this)return true;
		if (obj instanceof ServerSideConnection) {
			ServerSideConnection other = (ServerSideConnection)obj;
			if (other.getId() == this.getId() && other.isGeneric() == this.isGeneric() && getClientName().equals(other.getClientName()))return true;
		}
		return super.equals(obj);
	}
	
	public SSCWrapper getWrapper() {
		return new SSCWrapper(this);
	}
}
