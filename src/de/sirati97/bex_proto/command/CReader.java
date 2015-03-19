package de.sirati97.bex_proto.command;

import de.sirati97.bex_proto.ExtractorDat;

public class CReader implements CommandBase {
	private CommandBase command;

	public CReader(CommandBase command) {
		this.command = command;
	}

	@Override
	public Void extract(ExtractorDat dat) {
		return command.extract(dat);
	}

	@Override
	public short getId() {
		return 0;
	}

}
