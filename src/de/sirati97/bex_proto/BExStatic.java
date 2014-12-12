package de.sirati97.bex_proto;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
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
	
	public static byte[][] getStreamArray(List<Byte> data ,int startIndex) {
		List<byte[]> result = new ArrayList<>();
		byte[] dataArray = tobyteArray(data);
		int loc = startIndex;
		int fullArrayLenght = getInteger(dataArray, startIndex);
		loc += 4;
		for (int i=0;i<fullArrayLenght;i++) {
			if (loc >= dataArray.length)break;
			int elementArrayLenght  = getInteger(dataArray, + loc);
			byte[] element = new byte[elementArrayLenght];
			loc += 4;
			System.arraycopy(dataArray, loc, element, 0, (loc+elementArrayLenght>dataArray.length)?dataArray.length-loc:elementArrayLenght);
			loc += elementArrayLenght;
			result.add(element);
		}
		result.removeAll(Collections.singleton(null));
		return toStreamArray(result);
	}
	
	public static byte[] setStreamArray(byte[][] streams) {
		int resultLenght = 4;
		for (byte[] element:streams) {
			if (element != null && element.length > 0)resultLenght += 4+element.length;
		}
		byte[] result = new byte[resultLenght];
		byte[] tmp;
		int loc;
		tmp = setInteger(streams.length);
		System.arraycopy(tmp, 0, result, 0, tmp.length);
		loc = tmp.length;
		for (byte[] element:streams) {
			if (element != null && element.length > 0) {
				tmp = setInteger(element.length);
				System.arraycopy(tmp, 0, result, loc, tmp.length);
				loc += tmp.length;
				tmp = element;
				System.arraycopy(tmp, 0, result, loc, tmp.length);
				loc += tmp.length;
			}
			
		}
		return result;
	}
	

	public static byte[] mergeStream(byte[][] streams) {
		int resultLenght = 0;
		for (byte[] element:streams) {
			if (element != null && element.length > 0)resultLenght += 4+element.length;
		}
		byte[] result = new byte[resultLenght];
		byte[] tmp;
		int loc = 0;
		for (byte[] element:streams) {
			if (element != null && element.length > 0) {
//				tmp = setInteger(element.length);
//				System.arraycopy(tmp, 0, result, loc, tmp.length);
//				loc += tmp.length;
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
	
	public static byte[] setStringArray(String[] array, Charset charset) {
		byte[] result = setInteger(array.length);
		for (String str:array) {
			byte[] stream = setString(str, charset);
			byte[] temp = new byte[result.length + stream.length];
			System.arraycopy(result, 0, temp, 0, result.length);
			System.arraycopy(stream, 0, temp, result.length, stream.length);
 			result = temp;
		}
		return result;
	}
	
	
	
	public static String[] getStringArray(List<Byte> data ,int startIndex, Charset charset) {
		byte[] dataArray = tobyteArray(data);
		List<String> result = new ArrayList<String>();
		int temp = startIndex;
		//int arrayLenght = getInteger(data.get(startIndex+1), data.get(startIndex+1), data.get(startIndex+1), data.get(startIndex+1));
		int arrayLenght = getInteger(dataArray, startIndex);
		
		temp += 4;
		if (arrayLenght < 0)return new String[]{};
		for (int i = 0;i < arrayLenght;i++) {
			int streamPartLenght = getInteger(dataArray, temp);
			String streamPartEncoded = getString(data, temp, charset);
			result.add(streamPartEncoded);
			temp += 4+streamPartLenght;
		}
		return (String[]) result.toArray(new String[result.size()]);
	}
	

	public static String_Value getString_Value(List<Byte> data ,int startIndex, Charset charset) {
		byte[] dataStream = tobyteArray(data);
		int streamLenght = getInteger(dataStream, startIndex);
		byte[] stream = tobyteArray(data, startIndex + 4, streamLenght);
		return new String_Value(stream, new String(stream, charset), charset, stream.length + 4);
	}
	
	public static String getString(List<Byte> data ,int startIndex, Charset charset) {
		return getString_Value(data, startIndex, charset).str;
	}
	
	public static byte[] setString(String str , Charset charset) {
//		return str.getBytes(charset);
		
		byte[] strStream = str.getBytes(charset);
		byte[] dataStream = setInteger(strStream.length);
		byte[] result = new byte[dataStream.length + strStream.length];
		System.arraycopy(dataStream, 0, result, 0, dataStream.length);
		System.arraycopy(strStream, 0, result, dataStream.length,
				strStream.length);
		return result;
	}

	
	public static String getString(ExtractorDat dat, Charset charset) {
		int streamLenght = getInteger(dat.getMulti(4));
		byte[] stream = dat.getMulti(streamLenght);
		return new String(stream, charset);
	}
	
	
	
	public static short getShort(byte[] data ,int startIndex) {return getShort(data[startIndex], data[startIndex+1]);}
	public static short getShort(byte b1, byte b2) {
		return (short) (((b1 & 0xFF) << 8) | (b2 & 0xFF));
	}
	
	public static byte[] setShort(short value) {
		ByteBuffer buffer = ByteBuffer.allocate(2);
		buffer.putShort(value);
		return buffer.array();
	}
	
	public static int getInteger(byte[] data) {
		return getInteger(data, 0);
	}
	public static int getInteger(byte[] data ,int startIndex) {return getInteger(data[startIndex], data[startIndex+1], data[startIndex+2], data[startIndex+3] );}
	public static int getInteger(byte b1, byte b2, byte b3, byte b4) {
		return ((b1 & 0xFF) << 24) | ((b2 & 0xFF) << 16)
		        | ((b3 & 0xFF) << 8) | (b4 & 0xFF);
	}
	
	public static byte[] setInteger(int value) {
		ByteBuffer buffer = ByteBuffer.allocate(4);
		buffer.putInt(value);
		return buffer.array();
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

}
