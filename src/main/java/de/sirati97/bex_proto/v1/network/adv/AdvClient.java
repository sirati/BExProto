package de.sirati97.bex_proto.v1.network.adv;

import java.io.IOException;
import java.net.Socket;

import javax.crypto.Cipher;

import de.sirati97.bex_proto.DataHandler2.Stream;
import de.sirati97.bex_proto.v1.StreamReader;
import de.sirati97.bex_proto.v1.command.CommandBase;
import de.sirati97.bex_proto.v1.command.CommandRegisterBase;
import de.sirati97.bex_proto.v1.command.CommandSender;
import de.sirati97.bex_proto.v1.command.CommandWrapper;
import de.sirati97.bex_proto.v1.command.ConnectionInfo;
import de.sirati97.bex_proto.threading.AsyncHelper;
import de.sirati97.bex_proto.v1.network.ISocketFactory;
import de.sirati97.bex_proto.v1.network.NetClient;
import de.sirati97.bex_proto.v1.network.NetConnection;

public class AdvClient extends NetClient implements AdvCreator, IServerSideConnection{
	private CommandRegisterBase register;
	private String clientName;
	private boolean generic;
	private CloseConnectionCommand closeConnectionCommand;
	private int id = 0;
	private PingCommand pingCommand;
	private ClientRegCommand clientRegCommand;
	private CryptoContainer cryptoContainer;
	private CryptoHandshakeData cryptoHandshakeData;
	private ConnectionInfo noEncrytptionInfo = new ConnectionInfo() {
		@Override
		public Cipher getSendCipher() {
			return null;
		}
	};
	
	public AdvClient(AsyncHelper asyncHelper, String ip, int port, String clientName, boolean generic, CommandBase command, ISocketFactory socketFactory) {
		super(asyncHelper, ip, port, new StreamReader(new CommandSender(new CommandRegisterBase())), socketFactory);
		this.clientName = clientName;
		this.generic = generic;
		CommandSender sender = (CommandSender) getStreamReader().getExtractor();
		register = (CommandRegisterBase) sender.getCommand();
		register.register(new ApplicationDataCommandWrapper(command, (short) 0, this));
		register.register(clientRegCommand=new ClientRegCommand());
		register.register(closeConnectionCommand=new CloseConnectionCommand());
		register.register(new ClientCryptoCommand());
		register.register(pingCommand=new PingCommand(4));
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
			setRegistered(false);
			Socket socket = createSocket();
			Stream stream = clientRegCommand.generateSendableStream(clientRegCommand.send(getClientName(), getSubnet(), generic, id, getReconnectID()), noEncrytptionInfo);
			socket.getOutputStream().write(stream.getBytes());
			reconnectWith(socket);
			
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

	@Override
	public CryptoContainer getCryptoContainer() {
		return cryptoContainer;
	}
	
	public void setCryptoContainer(CryptoContainer cryptoContainer) {
		this.cryptoContainer = cryptoContainer;
	}
	
	public boolean offersEncryption() {
		return cryptoContainer!=null;
	}
	

	@Override
	public void setCryptoHandshakeData(CryptoHandshakeData data) {
		cryptoHandshakeData = data;
	}

	@Override
	public CryptoHandshakeData getCryptoHandshakeData() {
		return cryptoHandshakeData;
	}
	
	private static class ApplicationDataCommandWrapper extends CommandWrapper {
		private AdvClient client;
		
		public ApplicationDataCommandWrapper(CommandBase command, short id, AdvClient client) {
			super(command, id);
			this.client = client;
		}
		
		@Override
		protected void onSend(NetConnection... connections) {
			if (client.isRegistered()) return;
			while (!client.isRegistered() && client.isEnabled()) {
				try {
					Thread.sleep(0, 1);
				} catch (InterruptedException e) {e.printStackTrace();}
			}
		}
		
	}
}
