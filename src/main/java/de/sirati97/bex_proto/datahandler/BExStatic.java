package de.sirati97.bex_proto.datahandler;

import de.sirati97.bex_proto.util.CursorByteBuffer;
import de.sirati97.bex_proto.util.bytebuffer.ByteBuffer;
import de.sirati97.bex_proto.util.bytebuffer.ByteBufferSegment;

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
	
	public static ByteBuffer setString(String str , Charset charset) {
		byte[] strStream = str.getBytes(charset);
		ByteBuffer lengthStream = setInteger(strStream.length);
		return new ByteBuffer(lengthStream, new ByteBufferSegment(strStream));
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
	

	public static ByteBuffer setByteArray(byte[] bytes) {
		ByteBuffer length = BExStatic.setInteger(bytes.length);
		return new ByteBuffer(length, new ByteBufferSegment(bytes));
	}

	public static ByteBuffer setShort(short value) {
		java.nio.ByteBuffer buffer = java.nio.ByteBuffer.allocate(2);
		buffer.putShort(value);
		return new ByteBuffer(buffer.array());
	}
	
	public static short getShort(byte[] data) {
		java.nio.ByteBuffer buffer = java.nio.ByteBuffer.wrap(data);
		return buffer.getShort();
	}
	
	public static ByteBuffer setInteger(int value) {
		java.nio.ByteBuffer buffer = java.nio.ByteBuffer.allocate(4);
		buffer.putInt(value);
		return new ByteBuffer(buffer.array());
	}
	
	public static int getInteger(byte[] data) {
		java.nio.ByteBuffer buffer = java.nio.ByteBuffer.wrap(data);
		return buffer.getInt();
	}
	
	public static int getInteger(byte[] data, int startPos) {
		java.nio.ByteBuffer buffer = java.nio.ByteBuffer.wrap(data);
		buffer.position(startPos);
		return buffer.getInt();
	}
	
	
	public static long getLong(byte[] data) {
		java.nio.ByteBuffer buffer = java.nio.ByteBuffer.wrap(data);
		return buffer.getLong();
	}
	
	public static ByteBuffer setLong(long value) {
		java.nio.ByteBuffer buffer = java.nio.ByteBuffer.allocate(8);
		buffer.putLong(value);
		return new ByteBuffer(buffer.array());
	}
	

	public static byte getByte(CursorByteBuffer dat) {
		return dat.getOne();
	}

	public static ByteBuffer setByte(byte b) {
		return new ByteBuffer(new byte[]{b});
	}
	
	public static boolean getBoolean(CursorByteBuffer dat) {
		return dat.getOne()!=0;
	}

	public static  ByteBuffer setBoolean(boolean b) {
		return new ByteBuffer(new byte[]{b?(byte)1:(byte)0});
	}
	

	public static double getDouble(CursorByteBuffer dat) {
		return getDouble(dat.getMulti(8));
	}

	public static double getDouble(byte[] data) {
		java.nio.ByteBuffer buffer = java.nio.ByteBuffer.wrap(data);
		return buffer.getDouble();
	}
	

	public static ByteBuffer setDouble(double value) {
		java.nio.ByteBuffer buffer = java.nio.ByteBuffer.allocate(8);
		buffer.putDouble(value);
		return new ByteBuffer(buffer.array());
	}
	
	
	public static float getFloat(CursorByteBuffer dat) {
		return getFloat(dat.getMulti(8));
	}

	public static float getFloat(byte[] data) {
		java.nio.ByteBuffer buffer = java.nio.ByteBuffer.wrap(data);
		return buffer.getFloat();
	}
	

	public static ByteBuffer setFloat(float value) {
		java.nio.ByteBuffer buffer = java.nio.ByteBuffer.allocate(8);
		buffer.putFloat(value);
		return new ByteBuffer(buffer.array());
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
	public static final String VERSION_STRING = "2.0.12";
	public static final int VERSION_INT_MIN = 2;
	public static final int VERSION_INT_CURRENT = 2;


}
