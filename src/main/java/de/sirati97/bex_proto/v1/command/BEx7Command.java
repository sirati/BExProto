package de.sirati97.bex_proto.v1.command;

import de.sirati97.bex_proto.datahandler.IDerivedType;
import de.sirati97.bex_proto.util.CursorByteBuffer;
import de.sirati97.bex_proto.v1.stream.MultiStream;
import de.sirati97.bex_proto.v1.stream.Stream;
import de.sirati97.bex_proto.datahandler.Type;
import de.sirati97.bex_proto.datahandler.IType;
import de.sirati97.bex_proto.v1.network.NetConnection;
import de.sirati97.bex_proto.util.bytebuffer.ByteBuffer;

public class BEx7Command<t1,t2,t3,t4,t5,t6,t7> implements CommandBase{
	IType[] types;
	private short id;
	private CommandBase parent;
	
	public BEx7Command(short id, IType...types) {
		this.types = types;
		this.id = id;
	}
	
	@SuppressWarnings("unchecked")
	public Void decode(CursorByteBuffer dat) {
		Object[] r = new Object[7];
		int counter=0;
		for (IType type:types) {
			 Object tempObj = type.getDecoder().decode(dat);
			 if (type.isArray() && type instanceof IDerivedType) {
				 tempObj = ((IDerivedType) type).toPrimitiveArray(tempObj);
			 }
			 r[counter++] = tempObj;
		}
		receive((t1)r[0],(t2)r[1],(t3)r[2],(t4)r[3],(t5)r[4],(t6)r[5],(t7)r[6], (NetConnection) dat.getIConnection());
		return null;
	}
	
	public void receive(t1 arg1, t2 arg2, t3 arg3, t4 arg4, t5 arg5, t6 arg6, t7 arg7, NetConnection sender) {
		
	}


	public Stream send(t1 arg1, t2 arg2, t3 arg3, t4 arg4, t5 arg5, t6 arg6, t7 arg7) {
		ByteBuffer[] buffers = new ByteBuffer[types.length];
		switch (types.length) {
		case 7:
			buffers[6]=types[6].getEncoder().encodeIndependent(arg7);
		case 6:
			buffers[5]=types[5].getEncoder().encodeIndependent(arg6);
		case 5:
			buffers[4]=types[4].getEncoder().encodeIndependent(arg5);
		case 4:
			buffers[3]=types[3].getEncoder().encodeIndependent(arg4);
		case 3:
			buffers[2]=types[2].getEncoder().encodeIndependent(arg3);
		case 2:
			buffers[1]=types[1].getEncoder().encodeIndependent(arg2);
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
		getParent().send(new MultiStream(Type.Short.getEncoder().encodeIndependent(getId()), stream), connections);
	}

	@Override
	public void setParent(CommandBase parent) {
		this.parent = parent;
	}

	@Override
	public CommandBase getParent() {
		return parent;
	}
	

	public void send(t1 arg1, t2 arg2, t3 arg3, t4 arg4, t5 arg5, t6 arg6, t7 arg7, NetConnection... connections) {
		send(send(arg1, arg2, arg3, arg4, arg5, arg6, arg7), connections);
	}

	@Override
	public Stream generateSendableStream(Stream stream, ConnectionInfo receiver) {
		return getParent().generateSendableStream(new MultiStream(Type.Short.getEncoder().encodeIndependent(getId()), stream), receiver);
	}
	
}
