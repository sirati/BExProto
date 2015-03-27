package de.sirati97.bex_proto.network.adv;

import de.sirati97.bex_proto.StreamReader;
import de.sirati97.bex_proto.command.CommandBase;
import de.sirati97.bex_proto.command.CommandRegisterBase;
import de.sirati97.bex_proto.command.CommandSender;
import de.sirati97.bex_proto.command.CommandWrapper;
import de.sirati97.bex_proto.network.AsyncHelper;
import de.sirati97.bex_proto.network.NetClient;

public class AdvClient extends NetClient implements AdvCreator, IServerSideConnection{
	private CommandRegisterBase register;
	private String clientName;
	private boolean generic;
	private CloseConnectionCommand closeConnectionCommand;
	private int id = 0;
	
	public AdvClient(AsyncHelper asyncHelper, String ip, int port, String clientName, boolean generic, CommandBase command) {
		super(asyncHelper, ip, port, new StreamReader(new CommandSender(new CommandRegisterBase())));
		this.clientName = clientName;
		this.generic = generic;
		CommandSender sender = (CommandSender) getStreamReader().getExtractor();
		register = (CommandRegisterBase) sender.getCommand();
		register.register(new CommandWrapper(command, (short) 0));
		register.register(new ClientRegCommand());
		register.register(closeConnectionCommand=new CloseConnectionCommand());
		setRegistered(false);
	}
	
	protected CloseConnectionCommand getCloseConnectionCommand() {
		return closeConnectionCommand;
	}
	
	public void closeConnectionSoft() {
		getCloseConnectionCommand().send(this);
	}
	
	public String getClientName() {
		return clientName;
	}
	
	public boolean isGeneric() {
		return generic;
	}

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	@Override
	public ServerSideConnection getServerSideConnection(String clientName,
			boolean generic, int id) {
		return new ServerSideConnection(clientName, generic, id);
	}
	
	@Override
	public boolean equals(Object arg0) {
		if (arg0 instanceof ServerSideConnection) {
			return arg0.equals(this);
		}
		return super.equals(arg0);
	}
	
	public SSCWrapper toWrapper() {
		return new SSCWrapper(this);
	}

}
