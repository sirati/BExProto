package de.sirati97.bex_proto.datahandler_;

public class NullStream implements Stream {

	@Override
	public byte[] getBytes() {
		return new byte[]{};
	}

}
