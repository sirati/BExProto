package de.sirati97.bex_proto.v1.network.adv;

import de.sirati97.bex_proto.datahandler.Types;
import de.sirati97.bex_proto.v1.command.BEx5Command;
import de.sirati97.bex_proto.v1.network.NetConnection;

public class RegCommand
		extends
		BEx5Command<String, String, Boolean, Integer, Integer> {

	public RegCommand() {
		super((short) 1, Types.String_US_ASCII, Types.String_US_ASCII, Types.Boolean, Types.Integer, Types.Integer);
	}

	@Override
	public void receive(String name, String subnet, Boolean generic, Integer id, Integer reconnectID, NetConnection sender) {}
	
	@Override
	public void send(String name, String subnet, Boolean generic, Integer id, Integer reconnectID, NetConnection...connections) {
		super.send(name, subnet, generic, id, reconnectID, connections);
	}
}
