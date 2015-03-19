package de.sirati97.bex_proto.command;

import java.util.HashMap;
import java.util.Map;

import de.sirati97.bex_proto.ExtractorDat;
import de.sirati97.bex_proto.Type;

public class CommandRegister implements CommandBase {
	private Map<Short, CommandBase> commands = new HashMap<>();
	private short id;
	
	public CommandRegister(short id) {
		this.id = id;
	}
	
	@Override
	public Void extract(ExtractorDat dat) {
		short commandId = (Short) Type.Short.getExtractor().extract(dat);
		CommandBase command = commands.get(commandId);
		return command.extract(dat);
	}
	
	public void register(CommandBase command) {
		
	}

	@Override
	public short getId() {
		return id;
	}

}
