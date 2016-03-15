package de.sirati97.bex_proto.command;

import de.sirati97.bex_proto.Stream;
import de.sirati97.bex_proto.VoidExtractor;
import de.sirati97.bex_proto.network.NetConnection;

public interface CommandBase extends VoidExtractor{
	short getId();
	void setId(short id);
	void send(Stream stream, NetConnection...connections);
	Stream generateSendableStream(Stream stream, ConnectionInfo receiver);
	void setParent(CommandBase parent);
	CommandBase getParent();
}
