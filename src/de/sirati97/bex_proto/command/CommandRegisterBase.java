package de.sirati97.bex_proto.command;

import java.util.HashMap;
import java.util.Map;

import de.sirati97.bex_proto.ExtractorDat;
import de.sirati97.bex_proto.Stream;
import de.sirati97.bex_proto.Type;
import de.sirati97.bex_proto.network.NetConnection;

public class CommandRegisterBase implements CommandBase {
	private Map<Short, CommandBase> commands = new HashMap<>();
	private short id;
	private CommandBase parent;

	public CommandRegisterBase() {
		this((short) 0);
	}
	
	protected CommandRegisterBase(short id) {
		this.id = id;
	}
	
	@Override
	public Void extract(ExtractorDat dat) {
		short commandId = (Short) Type.Short.getExtractor().extract(dat);
		checkID(commandId, dat);
		CommandBase command = commands.get(commandId);
		if (command==null) {
			throw new IllegalStateException("There is no command handler registered for id " + commandId + " in " + getClass().toString());
		}
		return command.extract(dat);
	}
	
	protected void checkID(short commandId, ExtractorDat dat){}
	
	public void register(CommandBase command) {
		commands.put(command.getId(), command);
		command.setParent(this);
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
}
