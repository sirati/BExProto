package de.sirati97.bex_proto.network.adv;

public class ServerSideConnection implements IServerSideConnection {
	private String clientName;
	private boolean generic;
	private int id;
	private CryptoHandshakeData cryptoHandshakeData;
	
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
	
	public void setId(int id) {
		this.id = id;
	}

	public boolean isGeneric() {
		return generic;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this)return true;
		if (obj instanceof IServerSideConnection) {
			ServerSideConnection other = (ServerSideConnection)obj;
			if (other.getId() == this.getId() && other.isGeneric() == this.isGeneric() && getClientName().equals(other.getClientName()))return true;
		}
		return super.equals(obj);
	}
	
	public SSCWrapper toWrapper() {
		return new SSCWrapper(this);
	}

	@Override
	public void setCryptoHandshakeData(CryptoHandshakeData data) {
		cryptoHandshakeData = data;
	}

	@Override
	public CryptoHandshakeData getCryptoHandshakeData() {
		return cryptoHandshakeData;
	}
}
