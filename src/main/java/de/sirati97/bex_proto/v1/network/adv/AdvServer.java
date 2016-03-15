package de.sirati97.bex_proto.v1.network.adv;

import de.sirati97.bex_proto.threading.AsyncHelper;
import de.sirati97.bex_proto.util.ByteBuffer;
import de.sirati97.bex_proto.v1.StreamReader;
import de.sirati97.bex_proto.v1.command.CommandBase;
import de.sirati97.bex_proto.v1.command.CommandRegisterBase;
import de.sirati97.bex_proto.v1.command.CommandSender;
import de.sirati97.bex_proto.v1.command.CommandWrapper;
import de.sirati97.bex_proto.v1.network.ISocketFactory;
import de.sirati97.bex_proto.v1.network.NetConnection;
import de.sirati97.bex_proto.v1.network.NetServer;

import java.net.InetAddress;
import java.net.Socket;
import java.util.Random;

public class AdvServer extends NetServer implements AdvCreator{
	private AdvServerCommandRegister register;
	private ServerRegCommand serverRegCommand;
	private ConnectionManager connectionManager = new ConnectionManager();
	private CloseConnectionCommand closeConnectionCommand;
	private PingCommand pingCommand;
	private ServerCryptoCommand cryptoCommand;
	private Random rnd = new Random();
	private CryptoContainer cryptoContainer;

	public AdvServer(AsyncHelper asyncHelper, int port, InetAddress address, CommandBase command, ISocketFactory socketFactory) {
		super(asyncHelper, port, address, new StreamReader(new CommandSender(new AdvServerCommandRegister())), socketFactory);
		CommandSender sender = (CommandSender) getStreamReader().getExtractor();
		register = (AdvServerCommandRegister) sender.getCommand();
		register.setServer(this);
		register.register(new CommandWrapper(command, (short) 0));
		register.register(serverRegCommand= new ServerRegCommand(connectionManager, this));
		register.register(closeConnectionCommand=new CloseConnectionCommand());
		register.register(cryptoCommand=new ServerCryptoCommand());
		register.register(pingCommand=new PingCommand(4));
		
	}
	

	public AdvServer(AsyncHelper asyncHelper, int port, CommandBase command, ISocketFactory socketFactory) {
		this(asyncHelper, port, null, command, socketFactory);
	}

	public ServerRegCommand getServerRegCommand() {
		return serverRegCommand;
	}
	
	
	public CloseConnectionCommand getCloseConnectionCommand() {
		return closeConnectionCommand;
	}
	
	@Override
	protected void onPreConnected(NetConnection connection) {
		connection.setRegistered(false);
	}
	
	@Override
	protected void onConnected(NetConnection connection) {
		if (connection.getReconnectID()>0)return;
		connection.setReconnectID(rnd.nextInt(Integer.MAX_VALUE-1)+1);
		sendHandshakeRequest(connection);
	}

	@Override
	public AdvConnection getServerSideConnection(String clientName, boolean generic, int id) {
		for (AdvConnection connection: connectionManager.getConnections(clientName)) {
			if (connection.getId() == id) {
				return connection;
			}
		}
		return null;
	}
	
	public ConnectionManager getConnectionManager() {
		return connectionManager;
	}
	
	@Override
	public void sendPing(NetConnection connection) {
		pingCommand.ping(connection);
	}
	
	protected void sendHandshakeAccepted(AdvConnection connection) {
		connection.getNetConnection().setRegistered(true);
		System.out.println("Registered new connection" + connection.varsToString());
		getServerRegCommand().send("I", connection.getNetConnection().getSubnet(), connection.isGeneric(), connection.getId(), -1, connection.getNetConnection());
	}
	

	protected void sendHandshakeRequest(NetConnection connection) {
		getServerRegCommand().send("H", "", false, 0, connection.getReconnectID(), connection);
	}
	
	protected void sendEncyptionRequest(NetConnection connection) {
		cryptoCommand.send(CryptoCommand.States.Request, connection);
	}
	
	@Override
	public boolean isEnabled() {
		return super.isEnabled();
	}
	
	
	@Override
	public void onSocketClosed(NetConnection connection) {
		AdvConnection advConnection = connectionManager.getAdvConnection(connection);
		super.onSocketClosed(connection);
		if (advConnection!=null) {
			System.out.println("Closed connection" + advConnection.varsToString());
			connectionManager.unregister(advConnection);
		}
	}
	
	private static class AdvServerCommandRegister extends CommandRegisterBase {
		private AdvServer server;
		@Override protected boolean checkID(short commandId, ByteBuffer dat) {
			NetConnection connection = (NetConnection) dat.getIConnection();
			if (connection.isRegistered() || commandId != 0) return true;
			server.onConnected(connection);
			while (!connection.isRegistered() && !connection.isPassAlong()) {
				try {
					Thread.sleep(0, 1);
				} catch (InterruptedException e) {e.printStackTrace();}
			}
			if (connection.isPassAlong()) {
				dat.setCursor(0);
				connection.getPassAlong().exercuteInput(dat);
				return false;
			}
			return true;
		}

		public void setServer(AdvServer server) {
			this.server = server;
		}
		
		
	}

	public void onReConnected(AdvConnection oldAdvConnection, NetConnection newConnection) {
		Socket socket = newConnection.getSocket();
		NetConnection oldConnection = oldAdvConnection.getNetConnection();
		newConnection.passAlong(oldConnection);
		oldConnection.reconnectWith(socket);
		System.out.println(oldConnection.getSendCipher());
		sendHandshakeAccepted(oldAdvConnection);
	}


	@Override
	public CryptoContainer getCryptoContainer() {
		return cryptoContainer;
	}
	
	public void setCryptoContainer(CryptoContainer cryptoContainer) {
		this.cryptoContainer = cryptoContainer;
	}
	
	public boolean needEncryption() {
		return cryptoContainer!=null;
	}


	public boolean trust(String certificateAlias, NetConnection sender) {
		return true;
	}
}



























