package de.sirati97.bex_proto.v1.network.adv;

import de.sirati97.bex_proto.v1.network.NetConnection;

public class ClientRegCommand extends RegCommand {
	@Override
	public void receive(String name, String subnet, Boolean generic, Integer id, Integer reconnectID, NetConnection sender) {
		if (sender.isRegistered())return;
		
		if ("H".equals(name)) {
			AdvClient client = (AdvClient) sender;
			client.setReconnectID(reconnectID);
			send(client.getClientName(), client.getSubnet(), client.isGeneric(), 0, -1, sender);
		} else if ("I".equals(name)) {
			AdvClient client = (AdvClient) sender;
			client.setId(id);
			sender.setRegistered(true);
			System.out.println("Handshake completed!");
		}
	}
}
