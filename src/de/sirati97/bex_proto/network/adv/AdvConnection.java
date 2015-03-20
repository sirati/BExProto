package de.sirati97.bex_proto.network.adv;

import de.sirati97.bex_proto.network.NetConnection;

public class AdvConnection extends ServerSideConnection {
	private NetConnection netConnection;
	
	public AdvConnection(NetConnection netConnection, String clientName, boolean generic) {
		super(clientName, generic, 0);
		this.netConnection = netConnection;
	}
	
	
	public NetConnection getNetConnection() {
		return netConnection;
	}
	
	
	public AdvServer getServer() {
		return (AdvServer) getNetConnection().getCreator();
	}
	
	public void closeConnection() {
		getServer().getCloseConnectionCommand().send(getNetConnection());
	}
	
	
	public void setId(int id) {
		super.setId(id);
	}
}
