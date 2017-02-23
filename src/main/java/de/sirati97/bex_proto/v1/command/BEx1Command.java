package de.sirati97.bex_proto.v1.command;

import de.sirati97.bex_proto.datahandler.IDerivedType;
import de.sirati97.bex_proto.util.CursorByteBuffer;
import de.sirati97.bex_proto.v1.stream.MultiStream;
import de.sirati97.bex_proto.v1.stream.Stream;
import de.sirati97.bex_proto.datahandler.Types;
import de.sirati97.bex_proto.datahandler.IType;
import de.sirati97.bex_proto.v1.network.NetConnection;
import de.sirati97.bex_proto.util.bytebuffer.ByteBuffer;

public class BEx1Command<t1> extends CommandBase{
	IType[] types;
	private short id;
	private CommandBase parent;
	
	public BEx1Command(short id, IType...types) {
		this.types = types;
		this.id = id;
	}
	
	@SuppressWarnings("unchecked")
	public Void decode(CursorByteBuffer dat, boolean header) {
		Object[] r = new Object[1];
		int counter=0;
		for (IType type:types) {
			 Object tempObj = type.getDecoder().decode(dat);
			 if (type.isArray() && type instanceof IDerivedType) {
				 tempObj = ((IDerivedType) type).toPrimitiveArray(tempObj);
			 }
			 r[counter++] = tempObj;
		}
		receive((t1)r[0], (NetConnection) dat.getIConnection());
		return null;
	}
	
	public void receive(t1 arg1, NetConnection sender) {
		
	}


	public Stream send(t1 arg1) {
		ByteBuffer[] buffers = new ByteBuffer[types.length];
		switch (types.length) {
		case 1:
			buffers[0]=types[0].getEncoder().encodeIndependent(arg1);
		default:
			break;
		}
		return new MultiStream(buffers);
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
		getParent().send(new MultiStream(Types.Short.getEncoder().encodeIndependent(getId()), stream), connections);
	}

	@Override
	public void setParent(CommandBase parent) {
		this.parent = parent;
	}

	@Override
	public CommandBase getParent() {
		return parent;
	}
	

	public void send(t1 arg1, NetConnection... connections) {
		send(send(arg1), connections);
	}

	@Override
	public Stream generateSendableStream(Stream stream, ConnectionInfo receiver) {
		return getParent().generateSendableStream(new MultiStream(Types.Short.getEncoder().encodeIndependent(getId()), stream), receiver);
	}
	
}
