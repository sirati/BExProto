package de.sirati97.bex_proto.debug;

import de.sirati97.bex_proto.ArrayType;
import de.sirati97.bex_proto.Type;
import de.sirati97.bex_proto.command.BExCommand;

public class TestCommand extends BExCommand<String, String, String, String, Long, Integer, Short, Byte, Double, int[]> {

	public TestCommand() {
		super(Type.String_Utf_8, Type.String_Utf_16, Type.String_US_ASCII, Type.String_ISO_8859_1, Type.Long, Type.Integer, Type.Short, Type.Byte, Type.Double, new ArrayType(Type.Integer));
	}
	
	@Override
	public void receive(String arg1, String arg2, String arg3, String arg4, Long arg5, Integer arg6, Short arg7, Byte arg8, Double arg9, int[] arg10) {
		System.out.println(arg1);
		System.out.println(arg2);
		System.out.println(arg3);
		System.out.println(arg4);
		System.out.println(arg5);
		System.out.println(arg6);
		System.out.println(arg7);
		System.out.println(arg8);
		System.out.println(arg9);
		System.out.println(arg10);
	}
	
}
