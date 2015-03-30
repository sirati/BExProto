package de.sirati97.bex_proto.debug;

import de.sirati97.bex_proto.DynamicObj;
import de.sirati97.bex_proto.Type;
import de.sirati97.bex_proto.command.BEx10Command;
import de.sirati97.bex_proto.network.NetConnection;

public class MACommand extends BEx10Command<DynamicObj, Void, Void, Void, Void, Void, Void, Void, Void, Void> {

	
	public MACommand() {
		super((short)1, Type.DynamicObj);
	}
	
	@Override
	public void receive(DynamicObj arg1, Void arg2, Void arg3, Void arg4, Void arg5, Void arg6, Void arg7, Void arg8, Void arg9, Void arg10, NetConnection sender) {
		System.out.println(arg1.getValue()==null?"Object is null":arg1.getValue().toString());
			
	}
}
