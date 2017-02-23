package de.sirati97.bex_proto.v1.command;

import de.sirati97.bex_proto.datahandler.Types;
import de.sirati97.bex_proto.v1.network.NetConnection;
import de.sirati97.bex_proto.v1.stream.MultiStream;
import de.sirati97.bex_proto.v1.stream.Stream;

public class CommandRegister extends CommandRegisterBase {
	
	public CommandRegister(short id) {
		super(id);
	}
	
	public CommandRegister() {
		this((short)0);
	}

	@Override
	public void send(Stream stream, NetConnection... connections) {
		getParent().send(new MultiStream(Types.Short.getEncoder().encodeIndependent(getId()), stream), connections);
	}
	



	@Override
	public Stream generateSendableStream(Stream stream, ConnectionInfo receiver) {
		return getParent().generateSendableStream(new MultiStream(Types.Short.getEncoder().encodeIndependent(getId()), stream), receiver);
	}
}
