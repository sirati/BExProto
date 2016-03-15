package de.sirati97.bex_proto.command;

import de.sirati97.bex_proto.ExtractorDat;
import de.sirati97.bex_proto.Stream;
import de.sirati97.bex_proto.network.NetConnection;

public class CommandSBase implements CommandBase {
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
	public Void extract(ExtractorDat dat) {
		return command.extract(dat);
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
