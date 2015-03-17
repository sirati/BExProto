package de.sirati97.bex_proto.command;

import de.sirati97.bex_proto.ExtractorDat;

public class BExCommand<t1,t2,t3,t4,t5,t6,t7,t8,t9,t10> {
	TypeHandler[] handlers;
	Type[] types;
	
	public BExCommand(Type...types) {
		this.handlers = new TypeHandler[types.length>=10?10:types.length];
		this.types = new Type[handlers.length];
		int counter=0;
		for (Type type:types) {
			this.types[counter] = type;
			this.handlers[counter] = new TypeHandler(type);
			if (++counter>=10)return;
		}
	}
	
	@SuppressWarnings("unchecked")
	public void extrakt(ExtractorDat dat) {
		Object[] r = new Object[10];
		int counter=0;
		for (TypeHandler handler:handlers) {
			r[counter++] = handler.extract(dat);
		}
		receive((t1)r[0],(t2)r[1],(t3)r[2],(t4)r[3],(t5)r[4],(t6)r[5],(t7)r[6],(t8)r[7],(t9)r[8],(t10)r[9]);
	}
	
	public void receive(t1 arg1, t2 arg2, t3 arg3, t4 arg4, t5 arg5, t6 arg6, t7 arg7, t8 arg8, t9 arg9, t10 arg10) {
		
	}

}
