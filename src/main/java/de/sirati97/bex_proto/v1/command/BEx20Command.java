package de.sirati97.bex_proto.v1.command;

import de.sirati97.bex_proto.datahandler.DerivedTypeBase;
import de.sirati97.bex_proto.util.ByteBuffer;
import de.sirati97.bex_proto.datahandler.MultiStream;
import de.sirati97.bex_proto.datahandler.Stream;
import de.sirati97.bex_proto.datahandler.Type;
import de.sirati97.bex_proto.datahandler.TypeBase;
import de.sirati97.bex_proto.v1.network.NetConnection;

public class BEx20Command<t1,t2,t3,t4,t5,t6,t7,t8,t9,t10,t11,t12,t13,t14,t15,t16,t17,t18,t19,t20> implements CommandBase{
	TypeBase[] types;
	private short id;
	private CommandBase parent;
	
	public BEx20Command(short id, TypeBase...types) {
		this.types = types;
		this.id = id;
	}
	
	@SuppressWarnings("unchecked")
	public Void extract(ByteBuffer dat) {
		Object[] r = new Object[20];
		int counter=0;
		for (TypeBase type:types) {
			 Object tempObj = type.getExtractor().extract(dat);
			 if (type.isArray() && type instanceof DerivedTypeBase) {
				 tempObj = ((DerivedTypeBase) type).toPremitiveArray(tempObj);
			 }
			 r[counter++] = tempObj;
		}
		receive((t1)r[0],(t2)r[1],(t3)r[2],(t4)r[3],(t5)r[4],(t6)r[5],(t7)r[6],(t8)r[7],(t9)r[8],(t10)r[9],(t11)r[10],(t12)r[11],(t13)r[12],(t14)r[13],(t15)r[14],(t16)r[15],(t17)r[16],(t18)r[17],(t19)r[18],(t20)r[19], (NetConnection) dat.getIConnection());
		return null;
	}
	
	public void receive(t1 arg1, t2 arg2, t3 arg3, t4 arg4, t5 arg5, t6 arg6, t7 arg7, t8 arg8, t9 arg9, t10 arg10, t11 arg11, t12 arg12, t13 arg13, t14 arg14, t15 arg15, t16 arg16, t17 arg17, t18 arg18, t19 arg19, t20 arg20, NetConnection sender) {
		
	}


	public Stream send(t1 arg1, t2 arg2, t3 arg3, t4 arg4, t5 arg5, t6 arg6, t7 arg7, t8 arg8, t9 arg9, t10 arg10, t11 arg11, t12 arg12, t13 arg13, t14 arg14, t15 arg15, t16 arg16, t17 arg17, t18 arg18, t19 arg19, t20 arg20) {
		Stream[] streams = new Stream[types.length];
		switch (types.length) {
		case 20:
			streams[19]=types[19].createStream(arg20);
		case 19:
			streams[18]=types[18].createStream(arg19);
		case 18:
			streams[17]=types[17].createStream(arg18);
		case 17:
			streams[16]=types[16].createStream(arg17);
		case 16:
			streams[15]=types[15].createStream(arg16);
		case 15:
			streams[14]=types[14].createStream(arg15);
		case 14:
			streams[13]=types[13].createStream(arg14);
		case 13:
			streams[12]=types[12].createStream(arg13);
		case 12:
			streams[11]=types[11].createStream(arg12);
		case 11:
			streams[10]=types[10].createStream(arg11);
		case 10:
			streams[9]=types[9].createStream(arg10);
		case 9:
			streams[8]=types[8].createStream(arg9);
		case 8:
			streams[7]=types[7].createStream(arg8);
		case 7:
			streams[6]=types[6].createStream(arg7);
		case 6:
			streams[5]=types[5].createStream(arg6);
		case 5:
			streams[4]=types[4].createStream(arg5);
		case 4:
			streams[3]=types[3].createStream(arg4);
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
	

	public void send(t1 arg1, t2 arg2, t3 arg3, t4 arg4, t5 arg5, t6 arg6, t7 arg7, t8 arg8, t9 arg9, t10 arg10, t11 arg11, t12 arg12, t13 arg13, t14 arg14, t15 arg15, t16 arg16, t17 arg17, t18 arg18, t19 arg19, t20 arg20, NetConnection... connections) {
		send(send(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20), connections);
	}

	@Override
	public Stream generateSendableStream(Stream stream, ConnectionInfo receiver) {
		return getParent().generateSendableStream(new MultiStream(Type.Short.createStream(getId()),stream), receiver);
	}
	
}
