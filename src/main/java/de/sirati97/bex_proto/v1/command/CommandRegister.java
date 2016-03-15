package de.sirati97.bex_proto.v1.command;

import de.sirati97.bex_proto.DataHandler.MultiStream;
import de.sirati97.bex_proto.DataHandler.Stream;
import de.sirati97.bex_proto.DataHandler.Type;
import de.sirati97.bex_proto.v1.network.NetConnection;

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
	



	@Override
	public Stream generateSendableStream(Stream stream, ConnectionInfo receiver) {
		return getParent().generateSendableStream(new MultiStream(Type.Short.createStream(getId()),stream), receiver);
	}
}
