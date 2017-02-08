package de.sirati97.bex_proto.v1.command;

import de.sirati97.bex_proto.util.CursorByteBuffer;
import de.sirati97.bex_proto.v1.stream.ByteBufferStream;
import de.sirati97.bex_proto.v1.stream.Stream;
import de.sirati97.bex_proto.datahandler.Type;
import de.sirati97.bex_proto.v1.network.NetConnection;

public abstract class CommandNone extends CommandBase {
	private short id;
	private CommandBase parent;
	

	
	public CommandNone(short id) {
		this.id = id;
	}

	@Override
	public Void decode(CursorByteBuffer dat, boolean header) {
		receive((NetConnection) dat.getIConnection());
		return null;
	}
	
	public abstract void receive(NetConnection sender);

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
		getParent().send(stream, connections);
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
		getParent().send(new ByteBufferStream(Type.Short.getEncoder().encodeIndependent(getId())), connections);
	}

	@Override
	public Stream generateSendableStream(Stream stream, ConnectionInfo receiver) {
		return getParent().generateSendableStream(stream, receiver);
	}
}
