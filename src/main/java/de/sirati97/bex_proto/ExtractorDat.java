package de.sirati97.bex_proto;

import de.sirati97.bex_proto.network.NetConnection;

public class ExtractorDat {
	private byte[] bytes;
	private int location = 0;
	private NetConnection sender;
	
	public ExtractorDat(byte[] bytes, NetConnection sender) {
		this.bytes = bytes;
		this.sender = sender;
	}

	public byte getOne() {
		return bytes[location++];
	}
	
	
	public byte[] getMulti(int length) {
		byte[] result = new byte[length];
		System.arraycopy(bytes, location, result, 0, length);
		location += length;
		return result;
	}
	
	public void moveCursor(int offset) {
		location+=offset;
	}
	
	public void setCursor(int pos) {
		location = pos;
	}
	
	public NetConnection getSender() {
		return sender;
	}
	
	public int getLocation() {
		return location;
	}
	
	public byte[] getBytes() {
		return bytes;
	}
	
}
