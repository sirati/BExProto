package de.sirati97.bex_proto.network.adv;

public interface IServerSideConnection {
	public String getClientName();
	public int getId();
	void setId(int id);
	public boolean isGeneric();
	public SSCWrapper toWrapper();
	public void setCryptoHandshakeData(CryptoHandshakeData data);
	public CryptoHandshakeData getCryptoHandshakeData();
}
