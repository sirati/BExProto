package de.sirati97.bex_proto.v1.network.adv;

import de.sirati97.bex_proto.datahandler.Type;
import de.sirati97.bex_proto.util.CursorByteBuffer;
import de.sirati97.bex_proto.v1.command.BEx3Command;
import de.sirati97.bex_proto.v1.network.NetConnection;

public class EncryptionCommand extends BEx3Command<Byte, byte[], byte[]> {

	public EncryptionCommand() {
		super((short)3, Type.Byte, Type.Byte.asArray().asNullable(), Type.Byte.asArray().asNullable());
	}
	
	@Override
	public void receive(Byte state, byte[] data, byte[] data2, NetConnection sender) {
		if (state==States.Error.getId()) {
			onError(Type.String_US_ASCII.getDecoder().decode(new CursorByteBuffer(data, sender)), sender);
		} else if (state==States.Request.getId()) {
			onRequest(sender);
		} else if (state==States.PublicKey.getId()) {
			onReceivedPublicKey(data, sender);
		} else if (state==States.RequestSecretKey.getId()) {
			onRequestSecretKey(sender);
		} else if (state==States.SecretKey.getId()) {
			onReceivedSecretKey(data, data2, sender);
		} else if (state==States.Cancel.getId()) {
			onCancelHandshake(sender);
		} else if (state==States.Success.getId()) {
			onSuccess(sender);
		}
	}
	
	protected void onSuccess(NetConnection sender) {}
	protected void onRequestSecretKey(NetConnection sender) {}
	protected void onCancelHandshake(NetConnection sender) {}
	protected void onError(String message, NetConnection sender) {}
	protected void onRequest(NetConnection sender) {}
	protected void onReceivedPublicKey(byte[] data, NetConnection sender) {}
	protected void onReceivedSecretKey(byte[] dataKey, byte[] dataVector, NetConnection sender) {}
	

	public void send(States state, NetConnection... connections) {
		send(state, null, connections);
	}
	
	public void send(States state, byte[] data, NetConnection... connections) {
		send(state, data, null, connections);
	}
	
	public void send(States state, byte[] data, byte[] data2, NetConnection... connections) {
		send(state.getId(), data, data2, connections);
	}
	
	@Override
	public void send(Byte state, byte[] data, byte[] data2, NetConnection... connections) {
		super.send(state, data, data2, connections);
	}
	
	public void sendError(String error, NetConnection... connections) {
		send(States.Error, Type.String_US_ASCII.getEncoder().encodeIndependent(error).getBytes(), connections);
	}
	
	public static enum States{
		Error(-1),Request(0),PublicKey(1),RequestSecretKey(2),SecretKey(3),Cancel(4), Success(5);
		
		private byte id;
		
		States(int id) {
			this.id = (byte)id;
		}
		
		public byte getId() {
			return id;
		}
		
	}

}
