package de.sirati97.bex_proto.command;

public class CommandWrapper extends CommandSBase {

	public CommandWrapper(CommandBase command) {
		this(command, (short) 0);
	}
	
	public CommandWrapper(CommandBase command, short id) {
		super(command, id);
		command.setId(id);
	}

}
