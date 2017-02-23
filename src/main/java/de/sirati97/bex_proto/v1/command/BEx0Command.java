package de.sirati97.bex_proto.v1.command;

import de.sirati97.bex_proto.datahandler.Types;
import de.sirati97.bex_proto.util.CursorByteBuffer;
import de.sirati97.bex_proto.v1.network.NetConnection;
import de.sirati97.bex_proto.v1.stream.MultiStream;
import de.sirati97.bex_proto.v1.stream.NullStream;
import de.sirati97.bex_proto.v1.stream.Stream;

public class BEx0Command extends CommandBase{
	private short id;
	private CommandBase parent;
	
	public BEx0Command(short id) {
		this.id = id;
	}
	
	public Void decode(CursorByteBuffer dat, boolean header) {
		receive((NetConnection) dat.getIConnection());
		return null;
	}
	
	public void receive(NetConnection sender) {
		
	}


	public Stream send() {
		return new NullStream();
	}

	@Override
	public short getId() {
		return id;
	}


	@Override
	public void setId(short id) {
		this.id = id;
	}
	
	@Override
	public void send(Stream stream, NetConnection... connections) {
		getParent().send(new MultiStream(Types.Short.getEncoder().encodeIndependent(getId()), stream), connections);
	}

	@Override
	public void setParent(CommandBase parent) {
		this.parent = parent;
	}

	@Override
	public CommandBase getParent() {
		return parent;
	}
	

	public void send(NetConnection... connections) {
		send(send(), connections);
	}

	@Override
	public Stream generateSendableStream(Stream stream, ConnectionInfo receiver) {
		return getParent().generateSendableStream(new MultiStream(Types.Short.getEncoder().encodeIndependent(getId()), stream), receiver);
	}
	
}
