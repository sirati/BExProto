package de.sirati97.bex_proto.v1.command;

import de.sirati97.bex_proto.v1.stream.Stream;
import de.sirati97.bex_proto.datahandler.VoidDecoder;
import de.sirati97.bex_proto.v1.network.NetConnection;

public interface CommandBase extends VoidDecoder {
	short getId();
	void setId(short id);
	void send(Stream stream, NetConnection...connections);
	Stream generateSendableStream(Stream stream, ConnectionInfo receiver);
	void setParent(CommandBase parent);
	CommandBase getParent();
}
