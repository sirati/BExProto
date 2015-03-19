package de.sirati97.bex_proto.network.adv;

import de.sirati97.bex_proto.StreamReader;
import de.sirati97.bex_proto.command.CommandBase;
import de.sirati97.bex_proto.command.CommandRegisterBase;
import de.sirati97.bex_proto.command.CommandSender;
import de.sirati97.bex_proto.command.CommandWrapper;
import de.sirati97.bex_proto.network.AsyncHelper;
import de.sirati97.bex_proto.network.NetConnection;
import de.sirati97.bex_proto.network.NetServer;

public class AdvServer extends NetServer {
	private CommandRegisterBase register;
	private ServerRegCommand serverRegCommand;
	private ConnectionManager clientManager = new ConnectionManager();
	private CloseConnectionCommand closeConnectionCommand;
	
	public AdvServer(AsyncHelper asyncHelper, int port, CommandBase command) {
		super(asyncHelper, port, new StreamReader(new CommandSender(new CommandRegisterBase())));
		CommandSender sender = (CommandSender) getStreamReader().getExtractor();
		register = (CommandRegisterBase) sender.getCommand();
		register.register(new CommandWrapper(command, (short) 0));
		register.register(serverRegCommand= new ServerRegCommand(clientManager));
		register.register(closeConnectionCommand=new CloseConnectionCommand());
	}

	public ServerRegCommand getServerRegCommand() {
		return serverRegCommand;
	}
	
	
	public CloseConnectionCommand getCloseConnectionCommand() {
		return closeConnectionCommand;
	}
	
	
	@Override
	protected void onConnected(NetConnection connection) {
		getServerRegCommand().send("HOST", false, connection);
	}
}
