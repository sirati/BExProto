package de.sirati97.bex_proto.network.adv;

import de.sirati97.bex_proto.Type;
import de.sirati97.bex_proto.command.BEx10Command;
import de.sirati97.bex_proto.network.NetConnection;

public class RegCommand
		extends
		BEx10Command<String, Boolean, Void, Void, Void, Void, Void, Void, Void, Void> {

	public RegCommand() {
		super((short) 1, Type.String_US_ASCII, Type.Boolean);
	}

	@Override
	public void receive(String name, Boolean generic, Void arg3, Void arg4, Void arg5, Void arg6, Void arg7, Void arg8, Void arg9, Void arg10, NetConnection sender) {}
	
	public void send(String name, boolean generic, NetConnection...connections) {
		send(send(name, generic, null, null, null, null, null, null, null, null), connections);
	}
}
