package de.sirati97.bex_proto.command;

import org.apache.commons.lang3.ArrayUtils;

import de.sirati97.bex_proto.ArrayType;
import de.sirati97.bex_proto.ExtractorDat;
import de.sirati97.bex_proto.MultiStream;
import de.sirati97.bex_proto.PremitivType;
import de.sirati97.bex_proto.Stream;
import de.sirati97.bex_proto.Type;
import de.sirati97.bex_proto.TypeBase;
import de.sirati97.bex_proto.VoidExtractor;

public class BExCommand<t1,t2,t3,t4,t5,t6,t7,t8,t9,t10> implements VoidExtractor{
//	TypeHandler[] handlers;
	TypeBase[] types;
	
	public BExCommand(TypeBase...types) {
//		this.handlers = new TypeHandler[types.length>=10?10:types.length];
		this.types = types;
//		int counter=0;
//		for (TypeBase type:types) {
//			this.types[counter] = type;
//			this.handlers[counter] = new TypeHandler(type);
//			if (++counter>=10)return;
//		}
	}
	
	@SuppressWarnings("unchecked")
	public Void extract(ExtractorDat dat) {
		Object[] r = new Object[10];
		int counter=0;
		for (TypeBase type:types) {
			 Object tempObj = type.getExtractor().extract(dat);
			 if (type instanceof ArrayType) {
				 tempObj = ((ArrayType) type).toPremitiveArray(tempObj);
			 }
			 r[counter++] = tempObj;
		}
		receive((t1)r[0],(t2)r[1],(t3)r[2],(t4)r[3],(t5)r[4],(t6)r[5],(t7)r[6],(t8)r[7],(t9)r[8],(t10)r[9]);
		return null;
	}
	
	public void receive(t1 arg1, t2 arg2, t3 arg3, t4 arg4, t5 arg5, t6 arg6, t7 arg7, t8 arg8, t9 arg9, t10 arg10) {
		
	}


	public Stream send(t1 arg1, t2 arg2, t3 arg3, t4 arg4, t5 arg5, t6 arg6, t7 arg7, t8 arg8, t9 arg9, t10 arg10) {
		Stream[] streams = new Stream[types.length];
		switch (types.length) {
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

	
}
