package de.sirati97.bex_proto.network.adv;

import de.sirati97.bex_proto.StreamReader;
import de.sirati97.bex_proto.command.CommandRegisterBase;
import de.sirati97.bex_proto.command.CommandSender;
import de.sirati97.bex_proto.network.AsyncHelper;
import de.sirati97.bex_proto.network.NetClient;

public class AdvClient extends NetClient {
	private CommandRegisterBase register;
	private String clientName;
	private boolean generic;
	private CloseConnectionCommand closeConnectionCommand;
	
	public AdvClient(AsyncHelper asyncHelper, String ip, int port, String clientName, boolean generic) {
		super(asyncHelper, ip, port, new StreamReader(new CommandSender(new CommandRegisterBase())));
		this.clientName = clientName;
		this.generic = generic;
		CommandSender sender = (CommandSender) getStreamReader().getExtractor();
		register = (CommandRegisterBase) sender.getCommand();
		register.register(new ClientRegCommand());
		register.register(closeConnectionCommand=new CloseConnectionCommand());
	}
	
	public CloseConnectionCommand getCloseConnectionCommand() {
		return closeConnectionCommand;
	}
	
	public String getClientName() {
		return clientName;
	}
	
	public boolean isGeneric() {
		return generic;
	}

}
