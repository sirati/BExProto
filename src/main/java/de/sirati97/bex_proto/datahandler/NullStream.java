package de.sirati97.bex_proto.datahandler;

public class NullStream implements Stream {

	@Override
	public byte[] getBytes() {
		return new byte[]{};
	}

}
