package de.sirati97.bex_proto.util;

public class CursorByteBuffer {
	private byte[] bytes;
	private int location = 0;
	private IConnection IConnection;
	
	public CursorByteBuffer(byte[] bytes, IConnection IConnection) {
		this.bytes = bytes;
		this.IConnection = IConnection;
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
	
	public IConnection getIConnection() {
		return IConnection;
	}
	
	public int getLocation() {
		return location;
	}
	
	public byte[] getBytes() {
		return bytes;
	}
	
}
