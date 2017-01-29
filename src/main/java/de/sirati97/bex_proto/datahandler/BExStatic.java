package de.sirati97.bex_proto.datahandler;

import de.sirati97.bex_proto.util.CursorByteBuffer;
import de.sirati97.bex_proto.util.bytebuffer.ByteBuffer;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public final class BExStatic {
	private BExStatic() {}
	
	public static byte[] tobyteArray(List<Byte> list) {return tobyteArray(list,	list.size());}
	public static byte[] tobyteArray(List<Byte> list, int lenght) {return tobyteArray(list, 0, lenght);}
	public static byte[] tobyteArray(List<Byte> list, int start, int lenght) {
		//if (start + lenght > list.size())lenght = list.size() - start;
		byte[] result = new byte[lenght];
		for (int i = 0;i < lenght;i++) {
			result[i] = list.get(start + i);
		}
		return result;
	}
	
	public static byte[][] cleanStreamArray(final byte[][] v) {
		  int r, w, n = r = w = v.length;
		  while (r > 0) {
		    final byte[] b = v[--r];
		    if (!b.equals(null) && b.length != 0) {
		      v[--w] = b;
		    }
		  }
		  final byte[][] c = new byte[n -= w][];
		  System.arraycopy(v, w, c, 0, n);
		  return c;
	}

	public static byte[][] toStreamArray(List<byte[]> list) {
		byte[][] result = new byte[list.size()][];
		int i = 0;
		for (byte[] element:list) {
			result[i++] = element;
		}
		return result;
	}
	
//	public static byte[] setStreamArray(byte[][] streams) {
//		int resultLength = 4;
//		for (byte[] element:streams) {
//			if (element != null && element.length > 0)resultLength += 4+element.length;
//		}
//		byte[] result = new byte[resultLength];
//		byte[] tmp;
//		int loc;
//		tmp = setInteger(streams.length);
//		System.arraycopy(tmp, 0, result, 0, tmp.length);
//		loc = tmp.length;
//		for (byte[] element:streams) {
//			if (element != null && element.length > 0) {
//				tmp = setInteger(element.length);
//				System.arraycopy(tmp, 0, result, loc, tmp.length);
//				loc += tmp.length;
//				tmp = element;
//				System.arraycopy(tmp, 0, result, loc, tmp.length);
//				loc += tmp.length;
//			}
//
//		}
//		return result;
//	}
	

	public static byte[] mergeStream(byte[][] streams) {
		int resultLenght = 0;
		for (byte[] element:streams) {
			if (element != null && element.length > 0)resultLenght += element.length;
		}
		byte[] result = new byte[resultLenght];
		byte[] tmp;
		int loc = 0;
		for (byte[] element:streams) {
			if (element != null && element.length > 0) {
				tmp = element;
				System.arraycopy(tmp, 0, result, loc, tmp.length);
				loc += tmp.length;
			}
			
		}
		return result;
	}
	
	public static List<Byte> toByteList(byte[] stream) {
		List<Byte> result = new ArrayList<Byte>();
		for (byte b:stream) {result.add(b);}
		return result;
	}
	
	
	public static byte[] setSingleBoolean(boolean b1) {
		return new byte[] {(byte) (b1?1:0)};
	}
	
	public static boolean getSingleBoolean(List<Byte> data ,int startIndex) {
		byte[] dataArray = tobyteArray(data);
		return dataArray[startIndex]!=0;
	}
	
//	public static byte[] setStringArray(String[] array, Charset charset) {
//		byte[] result = setInteger(array.length);
//		for (String str:array) {
//			byte[] stream = setString(str, charset);
//			byte[] temp = new byte[result.length + stream.length];
//			System.arraycopy(result, 0, temp, 0, result.length);
//			System.arraycopy(stream, 0, temp, result.length, stream.length);
// 			result = temp;
//		}
//		return result;
//	}
	

	public static String getString(CursorByteBuffer dat , int startIndex, Charset charset) {
		int streamLength = getInteger(dat);
		byte[] stream = dat.getMulti(streamLength);//toByteArray(data, startIndex + 4, streamLength);
		return  new String(stream, charset);
	}
	
	public static void setString(String str ,Charset charset, ByteBuffer buffer) {
		byte[] strStream = str.getBytes(charset);
		setByteArray(strStream, buffer);
	}

	
	public static String getString(CursorByteBuffer dat, Charset charset) {
		int streamLenght = getInteger(dat);
		byte[] stream = dat.getMulti(streamLenght);
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
		BExStatic.setInteger(bytes.length, buffer);
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
	
	
	public static class String_Value {
		public final byte[] data;
		public final String str;
		public final Charset charset;
		public final int usedLength;
		
		
		public String_Value(byte[] data, String str, Charset charset, int usedLength) {
			this.data = data;
			this.str = str;
			this.charset = charset;
			this.usedLength = usedLength;
		}
	}
	public static final String VERSION_STRING = "2.0.14";
	public static final int VERSION_INT_MIN = 4;
	public static final int VERSION_INT_CURRENT = 4;


}
