package de.sirati97.bex_proto.v1.command;

import de.sirati97.bex_proto.datahandler.EncryptionStream;
import de.sirati97.bex_proto.datahandler.SendStream;
import de.sirati97.bex_proto.datahandler.Stream;
import de.sirati97.bex_proto.v1.network.NetConnection;

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
					byteStream2 = new SendStream(new EncryptionStream(sendStream.getHeadlessStream(), connection.getSendCipher())).getBytes();
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
			return new SendStream(new EncryptionStream(stream, receiver.getSendCipher()));
		}
	}

}
