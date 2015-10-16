package de.sirati97.bex_proto.command;

import de.sirati97.bex_proto.CryptoStream;
import de.sirati97.bex_proto.SendStream;
import de.sirati97.bex_proto.Stream;
import de.sirati97.bex_proto.network.NetConnection;

public class CommandSender extends CommandSBase {
	public CommandSender(CommandBase command) {
		super(command);
	}

	
	@Override
	public void send(Stream stream, NetConnection... connections) {
		SendStream sendStream = new SendStream(stream);
		byte[] byteStream = sendStream.getBytes();
		for (NetConnection connection:connections) {
			if (connection.getWriteCipher() == null) {
				connection.send(byteStream);
			} else {
				connection.send(new SendStream(new CryptoStream(sendStream.getInnerByteStream(), connection.getWriteCipher())).getBytes());
			}
			
		}
	}
	
}
