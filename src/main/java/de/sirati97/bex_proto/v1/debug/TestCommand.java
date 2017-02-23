package de.sirati97.bex_proto.v1.debug;

import de.sirati97.bex_proto.datahandler.Types;
import de.sirati97.bex_proto.v1.command.BEx10Command;
import de.sirati97.bex_proto.v1.network.NetConnection;

public class TestCommand extends BEx10Command<String, String, String, String, Long, Integer, Short, Byte, Double, int[][]> {

	public TestCommand() {
		super((short)2, Types.String_Utf_8, Types.String_Utf_16, Types.String_US_ASCII, Types.String_ISO_8859_1, Types.Long, Types.Integer, Types.Short, Types.Byte, Types.Double, Types.Integer.asArray().asArray());
	}
	
	@Override
	public void receive(String arg1, String arg2, String arg3, String arg4, Long arg5, Integer arg6, Short arg7, Byte arg8, Double arg9, int[][] arg10, NetConnection sender) {
		System.out.println(arg1);
		System.out.println(arg2);
		System.out.println(arg3);
		System.out.println(arg4);
		System.out.println(arg5);
		System.out.println(arg6);
		System.out.println(arg7);
		System.out.println(arg8);
		System.out.println(arg9);
		// Multidimensions Array Testen
		System.out.println(arg10[0][0]);
		System.out.println(arg10[1][0]);
		System.out.println(arg10[1][1]);
		System.out.println(arg10[2][0]);
	}
	
}
