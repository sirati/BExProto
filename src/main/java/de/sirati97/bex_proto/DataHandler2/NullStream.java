package de.sirati97.bex_proto.DataHandler2;

public class NullStream implements Stream {

	@Override
	public byte[] getBytes() {
		return new byte[]{};
	}

}
