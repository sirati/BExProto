package de.sirati97.bex_proto.network.adv;

import java.io.IOException;
import java.net.Socket;

import javax.crypto.SecretKey;

import de.sirati97.bex_proto.Stream;
import de.sirati97.bex_proto.StreamReader;
import de.sirati97.bex_proto.command.CommandBase;
import de.sirati97.bex_proto.command.CommandRegisterBase;
import de.sirati97.bex_proto.command.CommandSender;
import de.sirati97.bex_proto.command.CommandWrapper;
import de.sirati97.bex_proto.network.AsyncHelper;
import de.sirati97.bex_proto.network.ISocketFactory;
import de.sirati97.bex_proto.network.NetClient;
import de.sirati97.bex_proto.network.NetConnection;

public class AdvClient extends NetClient implements AdvCreator, IServerSideConnection{
	private CommandRegisterBase register;
	private String clientName;
	private boolean generic;
	private CloseConnectionCommand closeConnectionCommand;
	private int id = 0;
	private PingCommand pingCommand;
	private ClientRegCommand clientRegCommand;
	
	public AdvClient(AsyncHelper asyncHelper, String ip, int port, String clientName, boolean generic, CommandBase command, ISocketFactory socketFactory, SecretKey secretKey) {
		super(asyncHelper, ip, port, new StreamReader(new CommandSender(new CommandRegisterBase())), socketFactory, secretKey);
		this.clientName = clientName;
		this.generic = generic;
		CommandSender sender = (CommandSender) getStreamReader().getExtractor();
		register = (CommandRegisterBase) sender.getCommand();
		register.register(new CommandWrapper(command, (short) 0));
		register.register(clientRegCommand=new ClientRegCommand());
		register.register(closeConnectionCommand=new CloseConnectionCommand());
		register.register(pingCommand=new PingCommand(3));
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
	public void sendPing(NetConnection connection) {
		pingCommand.ping(connection);
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
	
	public void reconnect() {
		try {
			Socket socket = createSocket();
			Stream stream = clientRegCommand.generateSendableStream(clientRegCommand.send(getClientName(), generic, id, getReconnectID()), this);
			socket.getOutputStream().write(stream.getBytes());
			reconnectWith(socket);
			
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

}
