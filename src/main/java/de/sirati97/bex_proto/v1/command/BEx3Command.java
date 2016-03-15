package de.sirati97.bex_proto.v1.command;

import de.sirati97.bex_proto.datahandler.DerivedTypeBase;
import de.sirati97.bex_proto.util.ByteBuffer;
import de.sirati97.bex_proto.datahandler.MultiStream;
import de.sirati97.bex_proto.datahandler.Stream;
import de.sirati97.bex_proto.datahandler.Type;
import de.sirati97.bex_proto.datahandler.TypeBase;
import de.sirati97.bex_proto.v1.network.NetConnection;

public class BEx3Command<t1,t2,t3> implements CommandBase{
	TypeBase[] types;
	private short id;
	private CommandBase parent;
	
	public BEx3Command(short id, TypeBase...types) {
		this.types = types;
		this.id = id;
	}
	
	@SuppressWarnings("unchecked")
	public Void extract(ByteBuffer dat) {
		Object[] r = new Object[3];
		int counter=0;
		for (TypeBase type:types) {
			 Object tempObj = type.getExtractor().extract(dat);
			 if (type.isArray() && type instanceof DerivedTypeBase) {
				 tempObj = ((DerivedTypeBase) type).toPremitiveArray(tempObj);
			 }
			 r[counter++] = tempObj;
		}
		receive((t1)r[0],(t2)r[1],(t3)r[2], (NetConnection) dat.getIConnection());
		return null;
	}
	
	public void receive(t1 arg1, t2 arg2, t3 arg3 , NetConnection sender) {
		
	}


	public Stream send(t1 arg1, t2 arg2, t3 arg3 ) {
		Stream[] streams = new Stream[types.length];
		switch (types.length) {
		case 3:
			streams[2]=types[2].createStream(arg3);
		case 2:
			streams[1]=types[1].createStream(arg2);
		case 1:
			streams[0]=types[0].createStream(arg1);
		default:
			break;
		}
		return new MultiStream(streams);
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
	

	public void send(t1 arg1, t2 arg2, t3 arg3, NetConnection... connections) {
		send(send(arg1, arg2, arg3), connections);
	}

	@Override
	public Stream generateSendableStream(Stream stream, ConnectionInfo receiver) {
		return getParent().generateSendableStream(new MultiStream(Type.Short.createStream(getId()),stream), receiver);
	}
	
}
