package de.sirati97.bex_proto.v1.command;

import de.sirati97.bex_proto.v1.stream.Stream;
import de.sirati97.bex_proto.datahandler.VoidDecoder;
import de.sirati97.bex_proto.v1.network.NetConnection;

public abstract class CommandBase extends VoidDecoder {
	public abstract short getId();
    public abstract void setId(short id);
    public abstract void send(Stream stream, NetConnection...connections);
    public abstract Stream generateSendableStream(Stream stream, ConnectionInfo receiver);
    public abstract void setParent(CommandBase parent);
    public abstract CommandBase getParent();
}
