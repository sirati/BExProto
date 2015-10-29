package de.sirati97.bex_proto.network.adv;

import java.net.InetAddress;
import java.net.Socket;
import java.util.Random;

import javax.crypto.SecretKey;

import de.sirati97.bex_proto.ExtractorDat;
import de.sirati97.bex_proto.StreamReader;
import de.sirati97.bex_proto.command.CommandBase;
import de.sirati97.bex_proto.command.CommandRegisterBase;
import de.sirati97.bex_proto.command.CommandSender;
import de.sirati97.bex_proto.command.CommandWrapper;
import de.sirati97.bex_proto.network.AsyncHelper;
import de.sirati97.bex_proto.network.ISocketFactory;
import de.sirati97.bex_proto.network.NetConnection;
import de.sirati97.bex_proto.network.NetServer;

public class AdvServer extends NetServer implements AdvCreator{
	private AdvServerCommandRegister register;
	private ServerRegCommand serverRegCommand;
	private ConnectionManager connectionManager = new ConnectionManager();
	private CloseConnectionCommand closeConnectionCommand;
	private PingCommand pingCommand;
	private Random rnd = new Random();
	
	public AdvServer(AsyncHelper asyncHelper, int port, InetAddress address, CommandBase command, ISocketFactory socketFactory, SecretKey secretKey) {
		super(asyncHelper, port, address, new StreamReader(new CommandSender(new AdvServerCommandRegister())), socketFactory, secretKey);
		CommandSender sender = (CommandSender) getStreamReader().getExtractor();
		register = (AdvServerCommandRegister) sender.getCommand();
		register.setServer(this);
		register.register(new CommandWrapper(command, (short) 0));
		register.register(serverRegCommand= new ServerRegCommand(connectionManager));
		register.register(closeConnectionCommand=new CloseConnectionCommand());
		register.register(pingCommand=new PingCommand(3));
		
	}
	

	public AdvServer(AsyncHelper asyncHelper, int port, CommandBase command, ISocketFactory socketFactory, SecretKey secretKey) {
		this(asyncHelper, port, null, command, socketFactory, secretKey);
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
		getServerRegCommand().send("H", false, 0, connection.getReconnectID(), connection);
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
		@Override protected boolean checkID(short commandId, ExtractorDat dat) {
			if (dat.getSender().isRegistered() || commandId != 0) return true;
			server.onConnected(dat.getSender());
			while (!dat.getSender().isRegistered() && !dat.getSender().isPassAlong()) {
				try {
					Thread.sleep(0, 1);
				} catch (InterruptedException e) {e.printStackTrace();}
			}
			if (dat.getSender().isPassAlong()) {
				dat.setCursor(0);
				dat.getSender().getPassAlong().exercuteInput(dat);
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
	}
}



























