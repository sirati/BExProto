package de.sirati97.bex_proto.v1.network.adv;

import de.sirati97.bex_proto.v1.network.NetConnection;

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
	
	public String varsToString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{cilentName=");
		sb.append(this.getClientName());
		sb.append(",ip=");
		sb.append(this.getNetConnection().getInetAddress().getHostAddress());
		sb.append(",port=");
		sb.append(this.getNetConnection().getPort());
		sb.append(",generic=");
		sb.append(this.isGeneric());
		sb.append(",id=");
		sb.append(this.getId());
		sb.append("}");
		return sb.toString();
	}
	
	@Override
	public String toString() {
		return super.toString() + varsToString();
	}
	
	public void setReconnectID(int reconnectID) {
		getNetConnection().setReconnectID(reconnectID);
	}
	
	public int getReconnectID() {
		return getNetConnection().getReconnectID();
	}
}
