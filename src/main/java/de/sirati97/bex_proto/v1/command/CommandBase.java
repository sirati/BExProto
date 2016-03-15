package de.sirati97.bex_proto.v1.command;

import de.sirati97.bex_proto.DataHandler.Stream;
import de.sirati97.bex_proto.DataHandler.VoidExtractor;
import de.sirati97.bex_proto.v1.network.NetConnection;

public interface CommandBase extends VoidExtractor{
	short getId();
	void setId(short id);
	void send(Stream stream, NetConnection...connections);
	Stream generateSendableStream(Stream stream, ConnectionInfo receiver);
	void setParent(CommandBase parent);
	CommandBase getParent();
}
