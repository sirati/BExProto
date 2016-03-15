package de.sirati97.bex_proto.command;

import de.sirati97.bex_proto.CryptoStream;
import de.sirati97.bex_proto.SendStream;
import de.sirati97.bex_proto.Stream;
import de.sirati97.bex_proto.network.NetConnection;

public class CommandSender extends CommandSBase {
	private Object cryptoMutex = new Object();
	public CommandSender(CommandBase command) {
		super(command);
	}

	@Override
	public void send(Stream stream, NetConnection... connections) {
		SendStream sendStream = new SendStream(stream);
		byte[] byteStream = sendStream.getBytes();
		for (NetConnection connection : connections) {
			if (connection.getSendCipher() == null) {
				connection.send(byteStream);
			} else {
				byte[] byteStream2;
				synchronized (cryptoMutex) {
					byteStream2 = new SendStream(new CryptoStream(sendStream.getInnerByteStream(), connection.getSendCipher())).getBytes();
				}
				connection.send(byteStream2);
			}

		}
	}

	@Override
	public Stream generateSendableStream(Stream stream, ConnectionInfo receiver) {
		if (receiver.getSendCipher() == null) {
			return new SendStream(stream);
		} else {
			return new SendStream(new CryptoStream(stream, receiver.getSendCipher()));
		}
	}

}
