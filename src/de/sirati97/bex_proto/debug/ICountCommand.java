package de.sirati97.bex_proto.debug;

import de.sirati97.bex_proto.Type;
import de.sirati97.bex_proto.command.BEx1Command;
import de.sirati97.bex_proto.network.NetConnection;

public class ICountCommand extends BEx1Command<Integer> {

	public ICountCommand(int id, String name) {
		super((short)id, Type.Integer);
		sb = new StringBuilder(name);
	}

	StringBuilder sb;
	int counter = 0;
	@Override
	public void receive(Integer i, NetConnection sender) {
		synchronized (sb) {
			if (counter++%20==0)sb.append("\n");
			String strI = String.valueOf(i);
			for (int j=0;j<(4-strI.length());j++) {
				sb.append(' ');
			}
			sb.append(strI);
			sb.append(", ");
		}
	}
	
	@Override
	public String toString() {
		return sb.substring(0, sb.length()-2) + "\nCounted " +counter + " requests.";
	}
}
