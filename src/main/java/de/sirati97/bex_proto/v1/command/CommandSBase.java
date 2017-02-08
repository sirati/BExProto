package de.sirati97.bex_proto.v1.command;

import de.sirati97.bex_proto.util.CursorByteBuffer;
import de.sirati97.bex_proto.v1.stream.Stream;
import de.sirati97.bex_proto.v1.network.NetConnection;

public class CommandSBase extends CommandBase {
	private CommandBase command;
	private short id;
	private CommandBase parent;
	

	public CommandSBase(CommandBase command) {
		this(command, (short) 0);
	}
	
	public CommandSBase(CommandBase command, short id) {
		this.command = command;
		this.id = id;
		command.setParent(this);
	}

	@Override
	public Void decode(CursorByteBuffer dat, boolean header) {
		return command.decode(dat);
	}

	@Override
	public short getId() {
		return id;
	}

	@Override
	public void setId(short id) {
		this.id = id;
	}
	
	public CommandBase getCommand() {
		return command;
	}
	
	protected void onSend(NetConnection... connections){}
	
	@Override
	public void send(Stream stream, NetConnection... connections) {
		onSend();
		getParent().send(stream, connections);
	}

	@Override
	public Stream generateSendableStream(Stream stream, ConnectionInfo receiver) {
		return getParent().generateSendableStream(stream, receiver);
	}

	@Override
	public void setParent(CommandBase parent) {
		this.parent = parent;
	}

	@Override
	public CommandBase getParent() {
		return parent;
	}

}
