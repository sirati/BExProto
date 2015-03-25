package de.sirati97.bex_proto.network.adv;

import de.sirati97.bex_proto.command.CommandNone;
import de.sirati97.bex_proto.network.NetConnection;

public class CloseConnectionCommand extends CommandNone {

	public CloseConnectionCommand() {
		super((short)2);
	}

	@Override
	public void receive(NetConnection sender) {
		sender.stop();
//		if (sender.getCreator() instanceof AdvServer) {
//			AdvServer server = (AdvServer)sender.getCreator();
//			ConnectionManager connectionManager = server.getConnectionManager();
//			connectionManager.unregister(sender);
//		}
	}

}
