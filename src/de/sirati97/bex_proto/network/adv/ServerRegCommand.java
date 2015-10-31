package de.sirati97.bex_proto.network.adv;

import de.sirati97.bex_proto.network.NetConnection;

public class ServerRegCommand extends RegCommand {
	private ConnectionManager clientManager;
	private AdvServer server;
	
	public ServerRegCommand(ConnectionManager clientManager, AdvServer server) {
		this.clientManager = clientManager;
		this.server = server;
	}
	
	@Override
	public void receive(String name, Boolean generic, Integer id, Integer reconnectID, NetConnection sender) {
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
				AdvConnection connection = new AdvConnection(sender, name, generic);
				clientManager.register(connection);
				sender.setRegistered(true);
				System.out.println("Registered new connection" + connection.varsToString());
				if (server.needEncryption()) {
					server.sendEncyptionRequest(sender);
				} else {
					server.sendHandshakeAccepted(connection);
				}
			}
			
			
		}
	}
}
