package de.sirati97.bex_proto.v1.network.adv;

import de.sirati97.bex_proto.v1.network.NetConnection;

public class ServerRegCommand extends RegCommand {
	private ConnectionManager clientManager;
	private AdvServer server;
	
	public ServerRegCommand(ConnectionManager clientManager, AdvServer server) {
		this.clientManager = clientManager;
		this.server = server;
	}
	
	@Override
	public void receive(String name, String subnet, Boolean generic, Integer id, Integer reconnectID, NetConnection sender) {
		if (sender.isRegistered())return;
		synchronized (sender) {
			if (sender.isRegistered())return;
			if (reconnectID > 0) {
				AdvConnection connection = clientManager.getAdvConnection(name, id);
				if (connection.getReconnectID() == reconnectID) {
					AdvServer server = connection.getServer();
					server.onReConnected(connection, sender);
				} else {
					throw new IllegalAccessError("The connection " + sender.toString() + " tried to take over the connection " + connection.toString() + " without permission!");
				}
			} else {
				sender.setSubnet(subnet);
				AdvConnection connection = new AdvConnection(sender, name, generic);
				clientManager.register(connection);
				if (server.needEncryption()) {
					server.sendEncryptionRequest(sender);
				} else {
					server.sendHandshakeAccepted(connection);
				}
			}
			
			
		}
	}
}
