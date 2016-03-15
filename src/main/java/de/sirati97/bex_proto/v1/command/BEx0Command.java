package de.sirati97.bex_proto.v1.command;

import de.sirati97.bex_proto.util.ByteBuffer;
import de.sirati97.bex_proto.datahandler.MultiStream;
import de.sirati97.bex_proto.datahandler.NullStream;
import de.sirati97.bex_proto.datahandler.Stream;
import de.sirati97.bex_proto.datahandler.Type;
import de.sirati97.bex_proto.v1.network.NetConnection;

public class BEx0Command implements CommandBase{
	private short id;
	private CommandBase parent;
	
	public BEx0Command(short id) {
		this.id = id;
	}
	
	public Void extract(ByteBuffer dat) {
		receive(dat.getSender());
		return null;
	}
	
	public void receive(NetConnection sender) {
		
	}


	public Stream send() {
		return new NullStream();
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
		getParent().send(new MultiStream(Type.Short.createStream(getId()),stream), connections);
	}

	@Override
	public void setParent(CommandBase parent) {
		this.parent = parent;
	}

	@Override
	public CommandBase getParent() {
		return parent;
	}
	

	public void send(NetConnection... connections) {
		send(send(), connections);
	}

	@Override
	public Stream generateSendableStream(Stream stream, ConnectionInfo receiver) {
		return getParent().generateSendableStream(new MultiStream(Type.Short.createStream(getId()),stream), receiver);
	}
	
}
