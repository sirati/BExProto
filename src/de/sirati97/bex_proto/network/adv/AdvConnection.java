package de.sirati97.bex_proto.network.adv;

import de.sirati97.bex_proto.network.NetConnection;

public class AdvConnection {
	private String clientName;
	private boolean generic;
	private NetConnection netConnection;
	
	public AdvConnection(NetConnection netConnection, String clientName, boolean generic) {
		this.netConnection = netConnection;
		this.clientName = clientName;
		this.generic = generic;
	}
	
	public String getClientName() {
		return clientName;
	}
	
	public NetConnection getNetConnection() {
		return netConnection;
	}
	
	public boolean isGeneric() {
		return generic;
	}
	
	public void closeConnection() {
		
	}
}
