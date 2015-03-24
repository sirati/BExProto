package de.sirati97.bex_proto.command;

import de.sirati97.bex_proto.MultiStream;
import de.sirati97.bex_proto.Stream;
import de.sirati97.bex_proto.Type;
import de.sirati97.bex_proto.network.NetConnection;

public class CommandRegister extends CommandRegisterBase {
	
	public CommandRegister(short id) {
		super(id);
	}
	
	public CommandRegister() {
		this((short)0);
	}

	@Override
	public void send(Stream stream, NetConnection... connections) {
		getParent().send(new MultiStream(Type.Short.createStream(getId()),stream), connections);
	}
}
