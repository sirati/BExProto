package de.sirati97.bex_proto.command;

import de.sirati97.bex_proto.SendStream;
import de.sirati97.bex_proto.Stream;
import de.sirati97.bex_proto.network.NetConnection;

public class CommandSender extends CommandSBase {

	public CommandSender(CommandBase command) {
		super(command);
	}

	
	@Override
	public void send(Stream stream, NetConnection... connections) {
		byte[] byteStream = new SendStream(stream).getBytes();
		for (NetConnection connection:connections) {
			connection.send(byteStream);
		}
	}
}
