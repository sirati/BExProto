package de.sirati97.bex_proto.network.adv;

import de.sirati97.bex_proto.Type;
import de.sirati97.bex_proto.command.BEx4Command;
import de.sirati97.bex_proto.network.NetConnection;

public class RegCommand
		extends
		BEx4Command<String, Boolean, Integer, Integer> {

	public RegCommand() {
		super((short) 1, Type.String_US_ASCII, Type.Boolean, Type.Integer, Type.Integer);
	}

	@Override
	public void receive(String name, Boolean generic, Integer id, Integer reconnectID, NetConnection sender) {}
	
	@Override
	public void send(String name, Boolean generic, Integer id, Integer reconnectID, NetConnection...connections) {
		super.send(name, generic, id, reconnectID, connections);
	}
}
