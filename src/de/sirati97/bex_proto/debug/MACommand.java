package de.sirati97.bex_proto.debug;

import de.sirati97.bex_proto.ArrayType;
import de.sirati97.bex_proto.Type;
import de.sirati97.bex_proto.command.BEx10Command;
import de.sirati97.bex_proto.network.NetConnection;

public class MACommand extends BEx10Command<byte[][], Void, Void, Void, Void, Void, Void, Void, Void, Void> {

	
	public MACommand() {
		super((short)1, new ArrayType(new ArrayType(Type.Byte)));
	}
	
	@Override
	public void receive(byte[][] arg1, Void arg2, Void arg3, Void arg4, Void arg5, Void arg6, Void arg7, Void arg8, Void arg9, Void arg10, NetConnection sender) {
		
	}
}
