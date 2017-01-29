package de.sirati97.bex_proto.v1.command;

import de.sirati97.bex_proto.datahandler.Type;
import de.sirati97.bex_proto.util.CursorByteBuffer;
import de.sirati97.bex_proto.v1.network.NetConnection;
import de.sirati97.bex_proto.v1.stream.Stream;

import java.util.HashMap;
import java.util.Map;

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
	public Void decode(CursorByteBuffer dat) {
		short commandId = (Short) Type.Short.getDecoder().decode(dat);
		if (!checkID(commandId, dat))return null;
		CommandBase command = commands.get(commandId);
		if (command==null) {
			throw new IllegalStateException("There is no command handler registered for id " + commandId + " in " + getClass().toString());
		}
		return command.decode(dat);
	}
	
	protected boolean checkID(short commandId, CursorByteBuffer dat){return true;}
	
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
