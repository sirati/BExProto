package de.sirati97.bex_proto.debug;

import java.util.UUID;

import de.sirati97.bex_proto.Type;
import de.sirati97.bex_proto.command.BEx10Command;
import de.sirati97.bex_proto.network.NetConnection;

public class MACommand extends BEx10Command<UUID, Long, Void, Void, Void, Void, Void, Void, Void, Void> {

	
	public MACommand() {
		super((short)1, Type.UUID, Type.Long);
	}
	
	@Override
	public void receive(UUID arg1, Long arg2, Void arg3, Void arg4, Void arg5, Void arg6, Void arg7, Void arg8, Void arg9, Void arg10, NetConnection sender) {
		System.out.println(arg1.toString());
		System.out.println(arg2.toString());
			
	}
}
