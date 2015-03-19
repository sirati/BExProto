package de.sirati97.bex_proto.command;

import de.sirati97.bex_proto.StreamExtractor;
import de.sirati97.bex_proto.VoidExtractor;

public interface CommandBase extends VoidExtractor,  StreamExtractor<Void> {
	short getId();
}
