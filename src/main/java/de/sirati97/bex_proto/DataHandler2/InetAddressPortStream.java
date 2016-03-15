package de.sirati97.bex_proto.DataHandler2;

public class InetAddressPortStream implements Stream {
	private InetAddressPort data;
	
	public InetAddressPortStream(InetAddressPort data) {
		this.data = data;
	}

	@Override
	public byte[] getBytes() {
		Stream address = Type.InetAddress.createStream(data.getInetAddress());
		Stream port = Type.Integer.createStream(data.getPort());
		return new MultiStream(address,port).getBytes();
	}

}
