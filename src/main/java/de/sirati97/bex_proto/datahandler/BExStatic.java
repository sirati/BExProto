package de.sirati97.bex_proto.datahandler;

import de.sirati97.bex_proto.util.CursorByteBuffer;
import de.sirati97.bex_proto.util.bytebuffer.ByteBuffer;

import java.nio.charset.Charset;

@SuppressWarnings("WeakerAccess")
public final class BExStatic {
	private BExStatic() {}

	public static void setString(String str ,Charset charset, ByteBuffer buffer) {
        setString(str, charset, buffer, true);
    }
	public static void setString(String str ,Charset charset, ByteBuffer buffer, boolean header) {
		byte[] strStream = str.getBytes(charset);
		setByteArray(strStream, buffer, header);
	}

	
	public static String getString(CursorByteBuffer dat, Charset charset) {
	    return getString(dat, charset, true);
    }

	public static String getString(CursorByteBuffer dat, Charset charset, boolean header) {
		int streamLength = header?getInteger(dat):(dat.size()-dat.getLocation());
		byte[] stream = dat.getMulti(streamLength);
		return new String(stream, charset);
	}
	

	public static int getInteger(CursorByteBuffer dat) {
		return getInteger(dat.getMulti(4));
	}
	


	public static long getLong(CursorByteBuffer dat) {
		return getLong(dat.getMulti(8));
	}
	
	public static short getShort(CursorByteBuffer dat) {
		return getShort(dat.getMulti(2));
	}
	

	public static void setByteArray(byte[] bytes, ByteBuffer buffer) {
        setByteArray(bytes, buffer, true);
    }

	public static void setByteArray(byte[] bytes, ByteBuffer buffer, boolean header) {
		if (header) {
		    BExStatic.setInteger(bytes.length, buffer);
        }
        buffer.append(bytes);
	}

    public static void setShort(short value, ByteBuffer buffer) {
        java.nio.ByteBuffer nioBuffer = java.nio.ByteBuffer.allocate(2);
        nioBuffer.putShort(value);
        buffer.append(nioBuffer.array());
    }

    public static void insertShort(short value, ByteBuffer buffer) {
        java.nio.ByteBuffer nioBuffer = java.nio.ByteBuffer.allocate(2);
        nioBuffer.putShort(value);
        buffer.insertFirst(nioBuffer.array());
    }
	
	public static short getShort(byte[] data) {
		java.nio.ByteBuffer nioBuffer = java.nio.ByteBuffer.wrap(data);
		return nioBuffer.getShort();
	}

    public static void setInteger(int value, ByteBuffer buffer) {
        java.nio.ByteBuffer nioBuffer = java.nio.ByteBuffer.allocate(4);
        nioBuffer.putInt(value);
        buffer.append(nioBuffer.array());
    }

    public static void insertInteger(int value, ByteBuffer buffer) {
        java.nio.ByteBuffer nioBuffer = java.nio.ByteBuffer.allocate(4);
        nioBuffer.putInt(value);
        buffer.insertFirst(nioBuffer.array());
    }
	
	public static int getInteger(byte[] data) {
		java.nio.ByteBuffer nioBuffer = java.nio.ByteBuffer.wrap(data);
		return nioBuffer.getInt();
	}
	
	public static int getInteger(byte[] data, int startPos) {
		java.nio.ByteBuffer nioBuffer = java.nio.ByteBuffer.wrap(data);
		nioBuffer.position(startPos);
		return nioBuffer.getInt();
	}
	
	
	public static long getLong(byte[] data) {
		java.nio.ByteBuffer nioBuffer = java.nio.ByteBuffer.wrap(data);
		return nioBuffer.getLong();
	}
	
	public static void setLong(long value, ByteBuffer buffer) {
		java.nio.ByteBuffer nioBuffer = java.nio.ByteBuffer.allocate(8);
		nioBuffer.putLong(value);
		buffer.append(nioBuffer.array());
	}
	

	public static byte getByte(CursorByteBuffer dat) {
		return dat.getOne();
	}

	public static void setByte(byte b, ByteBuffer buffer) {
		buffer.append(new byte[]{b});
	}
	
	public static boolean getBoolean(CursorByteBuffer dat) {
		return dat.getOne()!=0;
	}

	public static  void setBoolean(boolean b, ByteBuffer buffer) {
		setByte(b?(byte)1:(byte)0, buffer);
	}
	

	public static double getDouble(CursorByteBuffer dat) {
		return getDouble(dat.getMulti(8));
	}

	public static double getDouble(byte[] data) {
		java.nio.ByteBuffer nioBuffer = java.nio.ByteBuffer.wrap(data);
		return nioBuffer.getDouble();
	}
	

	public static void setDouble(double value, ByteBuffer buffer) {
		java.nio.ByteBuffer nioBuffer = java.nio.ByteBuffer.allocate(8);
		nioBuffer.putDouble(value);
		buffer.append(nioBuffer.array());
	}
	
	
	public static float getFloat(CursorByteBuffer dat) {
		return getFloat(dat.getMulti(8));
	}

	public static float getFloat(byte[] data) {
		java.nio.ByteBuffer nioBuffer = java.nio.ByteBuffer.wrap(data);
		return nioBuffer.getFloat();
	}
	

	public static void setFloat(float value, ByteBuffer buffer) {
		java.nio.ByteBuffer nioBuffer = java.nio.ByteBuffer.allocate(8);
		nioBuffer.putFloat(value);
		buffer.append(nioBuffer.array());
	}

	public static final String VERSION_STRING = "2.1.03";
	public static final int VERSION_INT_MIN = 5;
	public static final int VERSION_INT_CURRENT = 5;

    public static void printStackTrace(Throwable t) {
        t.printStackTrace();
    }
}
