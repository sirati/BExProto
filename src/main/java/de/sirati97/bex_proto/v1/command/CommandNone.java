package de.sirati97.bex_proto.v1.command;

import de.sirati97.bex_proto.util.ByteBuffer;
import de.sirati97.bex_proto.datahandler_.Stream;
import de.sirati97.bex_proto.datahandler_.Type;
import de.sirati97.bex_proto.v1.network.NetConnection;

public abstract class CommandNone implements CommandBase {
	private short id;
	private CommandBase parent;
	

	
	public CommandNone(short id) {
		this.id = id;
	}

	@Override
	public Void extract(ByteBuffer dat) {
		receive(dat.getSender());
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
		getParent().send(Type.Short.createStream(getId()), connections);
	}

	@Override
	public Stream generateSendableStream(Stream stream, ConnectionInfo receiver) {
		return getParent().generateSendableStream(stream, receiver);
	}
}