
package org.apache.commons.lang3;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.commons.lang3.mutable.MutableInt;

@SuppressWarnings("unchecked")
public class ArrayUtils {
    public static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];
    public static final Class<?>[] EMPTY_CLASS_ARRAY = new Class[0];
    public static final String[] EMPTY_STRING_ARRAY = new String[0];
    public static final long[] EMPTY_LONG_ARRAY = new long[0];
    public static final Long[] EMPTY_LONG_OBJECT_ARRAY = new Long[0];
    public static final int[] EMPTY_INT_ARRAY = new int[0];
    public static final Integer[] EMPTY_INTEGER_OBJECT_ARRAY = new Integer[0];
    public static final short[] EMPTY_SHORT_ARRAY = new short[0];
    public static final Short[] EMPTY_SHORT_OBJECT_ARRAY = new Short[0];
    public static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
    public static final Byte[] EMPTY_BYTE_OBJECT_ARRAY = new Byte[0];
    public static final double[] EMPTY_DOUBLE_ARRAY = new double[0];
    public static final Double[] EMPTY_DOUBLE_OBJECT_ARRAY = new Double[0];
    public static final float[] EMPTY_FLOAT_ARRAY = new float[0];
    public static final Float[] EMPTY_FLOAT_OBJECT_ARRAY = new Float[0];
    public static final boolean[] EMPTY_BOOLEAN_ARRAY = new boolean[0];
    public static final Boolean[] EMPTY_BOOLEAN_OBJECT_ARRAY = new Boolean[0];
    public static final char[] EMPTY_CHAR_ARRAY = new char[0];
    public static final Character[] EMPTY_CHARACTER_OBJECT_ARRAY = new Character[0];
    public static final int INDEX_NOT_FOUND = -1;

    public ArrayUtils() {
    }

    public static String toString(Object array) {
        return toString(array, "{}");
    }

    public static String toString(Object array, String stringIfNull) {
        return array == null?stringIfNull:(new ToStringBuilder(array, ToStringStyle.SIMPLE_STYLE)).append(array).toString();
    }

    public static int hashCode(Object array) {
        return (new HashCodeBuilder()).append(array).toHashCode();
    }

    /** @deprecated */
    @Deprecated
    public static boolean isEquals(Object array1, Object array2) {
        return (new EqualsBuilder()).append(array1, array2).isEquals();
    }

    public static Map<Object, Object> toMap(Object[] array) {
        if(array == null) {
            return null;
        } else {
            HashMap<Object, Object> map = new HashMap<Object, Object>((int)((double)array.length * 1.5D));

            for(int i = 0; i < array.length; ++i) {
                Object object = array[i];
                if(object instanceof Entry) {
                    Entry<?, ?> entry = (Entry<?, ?>)object;
                    map.put(entry.getKey(), entry.getValue());
                } else {
                    if(!(object instanceof Object[])) {
                        throw new IllegalArgumentException("Array element " + i + ", \'" + object + "\', is neither of type Map.Entry nor an Array");
                    }

                    Object[] var5 = (Object[])((Object[])object);
                    if(var5.length < 2) {
                        throw new IllegalArgumentException("Array element " + i + ", \'" + object + "\', has a length less than 2");
                    }

                    map.put(var5[0], var5[1]);
                }
            }

            return map;
        }
    }

    public static <T> T[] toArray(T... items) {
        return items;
    }

    public static <T> T[] clone(T[] array) {
        return (T[]) (array == null?null:(Object[])array.clone());
    }

    public static long[] clone(long[] array) {
        return array == null?null:(long[])array.clone();
    }

    public static int[] clone(int[] array) {
        return array == null?null:(int[])array.clone();
    }

    public static short[] clone(short[] array) {
        return array == null?null:(short[])array.clone();
    }

    public static char[] clone(char[] array) {
        return array == null?null:(char[])array.clone();
    }

    public static byte[] clone(byte[] array) {
        return array == null?null:(byte[])array.clone();
    }

    public static double[] clone(double[] array) {
        return array == null?null:(double[])array.clone();
    }

    public static float[] clone(float[] array) {
        return array == null?null:(float[])array.clone();
    }

    public static boolean[] clone(boolean[] array) {
        return array == null?null:(boolean[])array.clone();
    }

    public static Object[] nullToEmpty(Object[] array) {
        return array != null && array.length != 0?array:EMPTY_OBJECT_ARRAY;
    }

    public static Class<?>[] nullToEmpty(Class<?>[] array) {
        return array != null && array.length != 0?array:EMPTY_CLASS_ARRAY;
    }

    public static String[] nullToEmpty(String[] array) {
        return array != null && array.length != 0?array:EMPTY_STRING_ARRAY;
    }

    public static long[] nullToEmpty(long[] array) {
        return array != null && array.length != 0?array:EMPTY_LONG_ARRAY;
    }

    public static int[] nullToEmpty(int[] array) {
        return array != null && array.length != 0?array:EMPTY_INT_ARRAY;
    }

    public static short[] nullToEmpty(short[] array) {
        return array != null && array.length != 0?array:EMPTY_SHORT_ARRAY;
    }

    public static char[] nullToEmpty(char[] array) {
        return array != null && array.length != 0?array:EMPTY_CHAR_ARRAY;
    }

    public static byte[] nullToEmpty(byte[] array) {
        return array != null && array.length != 0?array:EMPTY_BYTE_ARRAY;
    }

    public static double[] nullToEmpty(double[] array) {
        return array != null && array.length != 0?array:EMPTY_DOUBLE_ARRAY;
    }

    public static float[] nullToEmpty(float[] array) {
        return array != null && array.length != 0?array:EMPTY_FLOAT_ARRAY;
    }

    public static boolean[] nullToEmpty(boolean[] array) {
        return array != null && array.length != 0?array:EMPTY_BOOLEAN_ARRAY;
    }

    public static Long[] nullToEmpty(Long[] array) {
        return array != null && array.length != 0?array:EMPTY_LONG_OBJECT_ARRAY;
    }

    public static Integer[] nullToEmpty(Integer[] array) {
        return array != null && array.length != 0?array:EMPTY_INTEGER_OBJECT_ARRAY;
    }

    public static Short[] nullToEmpty(Short[] array) {
        return array != null && array.length != 0?array:EMPTY_SHORT_OBJECT_ARRAY;
    }

    public static Character[] nullToEmpty(Character[] array) {
        return array != null && array.length != 0?array:EMPTY_CHARACTER_OBJECT_ARRAY;
    }

    public static Byte[] nullToEmpty(Byte[] array) {
        return array != null && array.length != 0?array:EMPTY_BYTE_OBJECT_ARRAY;
    }

    public static Double[] nullToEmpty(Double[] array) {
        return array != null && array.length != 0?array:EMPTY_DOUBLE_OBJECT_ARRAY;
    }

    public static Float[] nullToEmpty(Float[] array) {
        return array != null && array.length != 0?array:EMPTY_FLOAT_OBJECT_ARRAY;
    }

    public static Boolean[] nullToEmpty(Boolean[] array) {
        return array != null && array.length != 0?array:EMPTY_BOOLEAN_OBJECT_ARRAY;
    }

    public static <T> T[] subarray(T[] array, int startIndexInclusive, int endIndexExclusive) {
        if(array == null) {
            return null;
        } else {
            if(startIndexInclusive < 0) {
                startIndexInclusive = 0;
            }

            if(endIndexExclusive > array.length) {
                endIndexExclusive = array.length;
            }

            int newSize = endIndexExclusive - startIndexInclusive;
            Class<?> type = array.getClass().getComponentType();
            Object[] subarray;
            if(newSize <= 0) {
                subarray = (Object[])((Object[])Array.newInstance(type, 0));
                return (T[]) subarray;
            } else {
                subarray = (Object[])((Object[])Array.newInstance(type, newSize));
                System.arraycopy(array, startIndexInclusive, subarray, 0, newSize);
                return (T[]) subarray;
            }
        }
    }

    public static long[] subarray(long[] array, int startIndexInclusive, int endIndexExclusive) {
        if(array == null) {
            return null;
        } else {
            if(startIndexInclusive < 0) {
                startIndexInclusive = 0;
            }

            if(endIndexExclusive > array.length) {
                endIndexExclusive = array.length;
            }

            int newSize = endIndexExclusive - startIndexInclusive;
            if(newSize <= 0) {
                return EMPTY_LONG_ARRAY;
            } else {
                long[] subarray = new long[newSize];
                System.arraycopy(array, startIndexInclusive, subarray, 0, newSize);
                return subarray;
            }
        }
    }

    public static int[] subarray(int[] array, int startIndexInclusive, int endIndexExclusive) {
        if(array == null) {
            return null;
        } else {
            if(startIndexInclusive < 0) {
                startIndexInclusive = 0;
            }

            if(endIndexExclusive > array.length) {
                endIndexExclusive = array.length;
            }

            int newSize = endIndexExclusive - startIndexInclusive;
            if(newSize <= 0) {
                return EMPTY_INT_ARRAY;
            } else {
                int[] subarray = new int[newSize];
                System.arraycopy(array, startIndexInclusive, subarray, 0, newSize);
                return subarray;
            }
        }
    }

    public static short[] subarray(short[] array, int startIndexInclusive, int endIndexExclusive) {
        if(array == null) {
            return null;
        } else {
            if(startIndexInclusive < 0) {
                startIndexInclusive = 0;
            }

            if(endIndexExclusive > array.length) {
                endIndexExclusive = array.length;
            }

            int newSize = endIndexExclusive - startIndexInclusive;
            if(newSize <= 0) {
                return EMPTY_SHORT_ARRAY;
            } else {
                short[] subarray = new short[newSize];
                System.arraycopy(array, startIndexInclusive, subarray, 0, newSize);
                return subarray;
            }
        }
    }

    public static char[] subarray(char[] array, int startIndexInclusive, int endIndexExclusive) {
        if(array == null) {
            return null;
        } else {
            if(startIndexInclusive < 0) {
                startIndexInclusive = 0;
            }

            if(endIndexExclusive > array.length) {
                endIndexExclusive = array.length;
            }

            int newSize = endIndexExclusive - startIndexInclusive;
            if(newSize <= 0) {
                return EMPTY_CHAR_ARRAY;
            } else {
                char[] subarray = new char[newSize];
                System.arraycopy(array, startIndexInclusive, subarray, 0, newSize);
                return subarray;
            }
        }
    }

    public static byte[] subarray(byte[] array, int startIndexInclusive, int endIndexExclusive) {
        if(array == null) {
            return null;
        } else {
            if(startIndexInclusive < 0) {
                startIndexInclusive = 0;
            }

            if(endIndexExclusive > array.length) {
                endIndexExclusive = array.length;
            }

            int newSize = endIndexExclusive - startIndexInclusive;
            if(newSize <= 0) {
                return EMPTY_BYTE_ARRAY;
            } else {
                byte[] subarray = new byte[newSize];
                System.arraycopy(array, startIndexInclusive, subarray, 0, newSize);
                return subarray;
            }
        }
    }

    public static double[] subarray(double[] array, int startIndexInclusive, int endIndexExclusive) {
        if(array == null) {
            return null;
        } else {
            if(startIndexInclusive < 0) {
                startIndexInclusive = 0;
            }

            if(endIndexExclusive > array.length) {
                endIndexExclusive = array.length;
            }

            int newSize = endIndexExclusive - startIndexInclusive;
            if(newSize <= 0) {
                return EMPTY_DOUBLE_ARRAY;
            } else {
                double[] subarray = new double[newSize];
                System.arraycopy(array, startIndexInclusive, subarray, 0, newSize);
                return subarray;
            }
        }
    }

    public static float[] subarray(float[] array, int startIndexInclusive, int endIndexExclusive) {
        if(array == null) {
            return null;
        } else {
            if(startIndexInclusive < 0) {
                startIndexInclusive = 0;
            }

            if(endIndexExclusive > array.length) {
                endIndexExclusive = array.length;
            }

            int newSize = endIndexExclusive - startIndexInclusive;
            if(newSize <= 0) {
                return EMPTY_FLOAT_ARRAY;
            } else {
                float[] subarray = new float[newSize];
                System.arraycopy(array, startIndexInclusive, subarray, 0, newSize);
                return subarray;
            }
        }
    }

    public static boolean[] subarray(boolean[] array, int startIndexInclusive, int endIndexExclusive) {
        if(array == null) {
            return null;
        } else {
            if(startIndexInclusive < 0) {
                startIndexInclusive = 0;
            }

            if(endIndexExclusive > array.length) {
                endIndexExclusive = array.length;
            }

            int newSize = endIndexExclusive - startIndexInclusive;
            if(newSize <= 0) {
                return EMPTY_BOOLEAN_ARRAY;
            } else {
                boolean[] subarray = new boolean[newSize];
                System.arraycopy(array, startIndexInclusive, subarray, 0, newSize);
                return subarray;
            }
        }
    }

    public static boolean isSameLength(Object[] array1, Object[] array2) {
        return (array1 != null || array2 == null || array2.length <= 0) && (array2 != null || array1 == null || array1.length <= 0) && (array1 == null || array2 == null || array1.length == array2.length);
    }

    public static boolean isSameLength(long[] array1, long[] array2) {
        return (array1 != null || array2 == null || array2.length <= 0) && (array2 != null || array1 == null || array1.length <= 0) && (array1 == null || array2 == null || array1.length == array2.length);
    }

    public static boolean isSameLength(int[] array1, int[] array2) {
        return (array1 != null || array2 == null || array2.length <= 0) && (array2 != null || array1 == null || array1.length <= 0) && (array1 == null || array2 == null || array1.length == array2.length);
    }

    public static boolean isSameLength(short[] array1, short[] array2) {
        return (array1 != null || array2 == null || array2.length <= 0) && (array2 != null || array1 == null || array1.length <= 0) && (array1 == null || array2 == null || array1.length == array2.length);
    }

    public static boolean isSameLength(char[] array1, char[] array2) {
        return (array1 != null || array2 == null || array2.length <= 0) && (array2 != null || array1 == null || array1.length <= 0) && (array1 == null || array2 == null || array1.length == array2.length);
    }

    public static boolean isSameLength(byte[] array1, byte[] array2) {
        return (array1 != null || array2 == null || array2.length <= 0) && (array2 != null || array1 == null || array1.length <= 0) && (array1 == null || array2 == null || array1.length == array2.length);
    }

    public static boolean isSameLength(double[] array1, double[] array2) {
        return (array1 != null || array2 == null || array2.length <= 0) && (array2 != null || array1 == null || array1.length <= 0) && (array1 == null || array2 == null || array1.length == array2.length);
    }

    public static boolean isSameLength(float[] array1, float[] array2) {
        return (array1 != null || array2 == null || array2.length <= 0) && (array2 != null || array1 == null || array1.length <= 0) && (array1 == null || array2 == null || array1.length == array2.length);
    }

    public static boolean isSameLength(boolean[] array1, boolean[] array2) {
        return (array1 != null || array2 == null || array2.length <= 0) && (array2 != null || array1 == null || array1.length <= 0) && (array1 == null || array2 == null || array1.length == array2.length);
    }

    public static int getLength(Object array) {
        return array == null?0:Array.getLength(array);
    }

    public static boolean isSameType(Object array1, Object array2) {
        if(array1 != null && array2 != null) {
            return array1.getClass().getName().equals(array2.getClass().getName());
        } else {
            throw new IllegalArgumentException("The Array must not be null");
        }
    }

    public static void reverse(Object[] array) {
        if(array != null) {
            reverse((Object[])array, 0, array.length);
        }
    }

    public static void reverse(long[] array) {
        if(array != null) {
            reverse((long[])array, 0, array.length);
        }
    }

    public static void reverse(int[] array) {
        if(array != null) {
            reverse((int[])array, 0, array.length);
        }
    }

    public static void reverse(short[] array) {
        if(array != null) {
            reverse((short[])array, 0, array.length);
        }
    }

    public static void reverse(char[] array) {
        if(array != null) {
            reverse((char[])array, 0, array.length);
        }
    }

    public static void reverse(byte[] array) {
        if(array != null) {
            reverse((byte[])array, 0, array.length);
        }
    }

    public static void reverse(double[] array) {
        if(array != null) {
            reverse((double[])array, 0, array.length);
        }
    }

    public static void reverse(float[] array) {
        if(array != null) {
            reverse((float[])array, 0, array.length);
        }
    }

    public static void reverse(boolean[] array) {
        if(array != null) {
            reverse((boolean[])array, 0, array.length);
        }
    }

    public static void reverse(boolean[] array, int startIndexInclusive, int endIndexExclusive) {
        if(array != null) {
            int i = startIndexInclusive < 0?0:startIndexInclusive;

            for(int j = Math.min(array.length, endIndexExclusive) - 1; j > i; ++i) {
                boolean tmp = array[j];
                array[j] = array[i];
                array[i] = tmp;
                --j;
            }

        }
    }

    public static void reverse(byte[] array, int startIndexInclusive, int endIndexExclusive) {
        if(array != null) {
            int i = startIndexInclusive < 0?0:startIndexInclusive;

            for(int j = Math.min(array.length, endIndexExclusive) - 1; j > i; ++i) {
                byte tmp = array[j];
                array[j] = array[i];
                array[i] = tmp;
                --j;
            }

        }
    }

    public static void reverse(char[] array, int startIndexInclusive, int endIndexExclusive) {
        if(array != null) {
            int i = startIndexInclusive < 0?0:startIndexInclusive;

            for(int j = Math.min(array.length, endIndexExclusive) - 1; j > i; ++i) {
                char tmp = array[j];
                array[j] = array[i];
                array[i] = tmp;
                --j;
            }

        }
    }

    public static void reverse(double[] array, int startIndexInclusive, int endIndexExclusive) {
        if(array != null) {
            int i = startIndexInclusive < 0?0:startIndexInclusive;

            for(int j = Math.min(array.length, endIndexExclusive) - 1; j > i; ++i) {
                double tmp = array[j];
                array[j] = array[i];
                array[i] = tmp;
                --j;
            }

        }
    }

    public static void reverse(float[] array, int startIndexInclusive, int endIndexExclusive) {
        if(array != null) {
            int i = startIndexInclusive < 0?0:startIndexInclusive;

            for(int j = Math.min(array.length, endIndexExclusive) - 1; j > i; ++i) {
                float tmp = array[j];
                array[j] = array[i];
                array[i] = tmp;
                --j;
            }

        }
    }

    public static void reverse(int[] array, int startIndexInclusive, int endIndexExclusive) {
        if(array != null) {
            int i = startIndexInclusive < 0?0:startIndexInclusive;

            for(int j = Math.min(array.length, endIndexExclusive) - 1; j > i; ++i) {
                int tmp = array[j];
                array[j] = array[i];
                array[i] = tmp;
                --j;
            }

        }
    }

    public static void reverse(long[] array, int startIndexInclusive, int endIndexExclusive) {
        if(array != null) {
            int i = startIndexInclusive < 0?0:startIndexInclusive;

            for(int j = Math.min(array.length, endIndexExclusive) - 1; j > i; ++i) {
                long tmp = array[j];
                array[j] = array[i];
                array[i] = tmp;
                --j;
            }

        }
    }

    public static void reverse(Object[] array, int startIndexInclusive, int endIndexExclusive) {
        if(array != null) {
            int i = startIndexInclusive < 0?0:startIndexInclusive;

            for(int j = Math.min(array.length, endIndexExclusive) - 1; j > i; ++i) {
                Object tmp = array[j];
                array[j] = array[i];
                array[i] = tmp;
                --j;
            }

        }
    }

    public static void reverse(short[] array, int startIndexInclusive, int endIndexExclusive) {
        if(array != null) {
            int i = startIndexInclusive < 0?0:startIndexInclusive;

            for(int j = Math.min(array.length, endIndexExclusive) - 1; j > i; ++i) {
                short tmp = array[j];
                array[j] = array[i];
                array[i] = tmp;
                --j;
            }

        }
    }

    public static int indexOf(Object[] array, Object objectToFind) {
        return indexOf(array, objectToFind, 0);
    }

    public static int indexOf(Object[] array, Object objectToFind, int startIndex) {
        if(array == null) {
            return -1;
        } else {
            if(startIndex < 0) {
                startIndex = 0;
            }

            int i;
            if(objectToFind == null) {
                for(i = startIndex; i < array.length; ++i) {
                    if(array[i] == null) {
                        return i;
                    }
                }
            } else if(array.getClass().getComponentType().isInstance(objectToFind)) {
                for(i = startIndex; i < array.length; ++i) {
                    if(objectToFind.equals(array[i])) {
                        return i;
                    }
                }
            }

            return -1;
        }
    }

    public static int lastIndexOf(Object[] array, Object objectToFind) {
        return lastIndexOf(array, objectToFind, 2147483647);
    }

    public static int lastIndexOf(Object[] array, Object objectToFind, int startIndex) {
        if(array == null) {
            return -1;
        } else if(startIndex < 0) {
            return -1;
        } else {
            if(startIndex >= array.length) {
                startIndex = array.length - 1;
            }

            int i;
            if(objectToFind == null) {
                for(i = startIndex; i >= 0; --i) {
                    if(array[i] == null) {
                        return i;
                    }
                }
            } else if(array.getClass().getComponentType().isInstance(objectToFind)) {
                for(i = startIndex; i >= 0; --i) {
                    if(objectToFind.equals(array[i])) {
                        return i;
                    }
                }
            }

            return -1;
        }
    }

    public static boolean contains(Object[] array, Object objectToFind) {
        return indexOf(array, objectToFind) != -1;
    }

    public static int indexOf(long[] array, long valueToFind) {
        return indexOf(array, valueToFind, 0);
    }

    public static int indexOf(long[] array, long valueToFind, int startIndex) {
        if(array == null) {
            return -1;
        } else {
            if(startIndex < 0) {
                startIndex = 0;
            }

            for(int i = startIndex; i < array.length; ++i) {
                if(valueToFind == array[i]) {
                    return i;
                }
            }

            return -1;
        }
    }

    public static int lastIndexOf(long[] array, long valueToFind) {
        return lastIndexOf(array, valueToFind, 2147483647);
    }

    public static int lastIndexOf(long[] array, long valueToFind, int startIndex) {
        if(array == null) {
            return -1;
        } else if(startIndex < 0) {
            return -1;
        } else {
            if(startIndex >= array.length) {
                startIndex = array.length - 1;
            }

            for(int i = startIndex; i >= 0; --i) {
                if(valueToFind == array[i]) {
                    return i;
                }
            }

            return -1;
        }
    }

    public static boolean contains(long[] array, long valueToFind) {
        return indexOf(array, valueToFind) != -1;
    }

    public static int indexOf(int[] array, int valueToFind) {
        return indexOf((int[])array, (int)valueToFind, 0);
    }

    public static int indexOf(int[] array, int valueToFind, int startIndex) {
        if(array == null) {
            return -1;
        } else {
            if(startIndex < 0) {
                startIndex = 0;
            }

            for(int i = startIndex; i < array.length; ++i) {
                if(valueToFind == array[i]) {
                    return i;
                }
            }

            return -1;
        }
    }

    public static int lastIndexOf(int[] array, int valueToFind) {
        return lastIndexOf((int[])array, (int)valueToFind, 2147483647);
    }

    public static int lastIndexOf(int[] array, int valueToFind, int startIndex) {
        if(array == null) {
            return -1;
        } else if(startIndex < 0) {
            return -1;
        } else {
            if(startIndex >= array.length) {
                startIndex = array.length - 1;
            }

            for(int i = startIndex; i >= 0; --i) {
                if(valueToFind == array[i]) {
                    return i;
                }
            }

            return -1;
        }
    }

    public static boolean contains(int[] array, int valueToFind) {
        return indexOf((int[])array, (int)valueToFind) != -1;
    }

    public static int indexOf(short[] array, short valueToFind) {
        return indexOf((short[])array, (short)valueToFind, 0);
    }

    public static int indexOf(short[] array, short valueToFind, int startIndex) {
        if(array == null) {
            return -1;
        } else {
            if(startIndex < 0) {
                startIndex = 0;
            }

            for(int i = startIndex; i < array.length; ++i) {
                if(valueToFind == array[i]) {
                    return i;
                }
            }

            return -1;
        }
    }

    public static int lastIndexOf(short[] array, short valueToFind) {
        return lastIndexOf((short[])array, (short)valueToFind, 2147483647);
    }

    public static int lastIndexOf(short[] array, short valueToFind, int startIndex) {
        if(array == null) {
            return -1;
        } else if(startIndex < 0) {
            return -1;
        } else {
            if(startIndex >= array.length) {
                startIndex = array.length - 1;
            }

            for(int i = startIndex; i >= 0; --i) {
                if(valueToFind == array[i]) {
                    return i;
                }
            }

            return -1;
        }
    }

    public static boolean contains(short[] array, short valueToFind) {
        return indexOf((short[])array, (short)valueToFind) != -1;
    }

    public static int indexOf(char[] array, char valueToFind) {
        return indexOf((char[])array, (char)valueToFind, 0);
    }

    public static int indexOf(char[] array, char valueToFind, int startIndex) {
        if(array == null) {
            return -1;
        } else {
            if(startIndex < 0) {
                startIndex = 0;
            }

            for(int i = startIndex; i < array.length; ++i) {
                if(valueToFind == array[i]) {
                    return i;
                }
            }

            return -1;
        }
    }

    public static int lastIndexOf(char[] array, char valueToFind) {
        return lastIndexOf((char[])array, (char)valueToFind, 2147483647);
    }

    public static int lastIndexOf(char[] array, char valueToFind, int startIndex) {
        if(array == null) {
            return -1;
        } else if(startIndex < 0) {
            return -1;
        } else {
            if(startIndex >= array.length) {
                startIndex = array.length - 1;
            }

            for(int i = startIndex; i >= 0; --i) {
                if(valueToFind == array[i]) {
                    return i;
                }
            }

            return -1;
        }
    }

    public static boolean contains(char[] array, char valueToFind) {
        return indexOf((char[])array, (char)valueToFind) != -1;
    }

    public static int indexOf(byte[] array, byte valueToFind) {
        return indexOf((byte[])array, (byte)valueToFind, 0);
    }

    public static int indexOf(byte[] array, byte valueToFind, int startIndex) {
        if(array == null) {
            return -1;
        } else {
            if(startIndex < 0) {
                startIndex = 0;
            }

            for(int i = startIndex; i < array.length; ++i) {
                if(valueToFind == array[i]) {
                    return i;
                }
            }

            return -1;
        }
    }

    public static int lastIndexOf(byte[] array, byte valueToFind) {
        return lastIndexOf((byte[])array, (byte)valueToFind, 2147483647);
    }

    public static int lastIndexOf(byte[] array, byte valueToFind, int startIndex) {
        if(array == null) {
            return -1;
        } else if(startIndex < 0) {
            return -1;
        } else {
            if(startIndex >= array.length) {
                startIndex = array.length - 1;
            }

            for(int i = startIndex; i >= 0; --i) {
                if(valueToFind == array[i]) {
                    return i;
                }
            }

            return -1;
        }
    }

    public static boolean contains(byte[] array, byte valueToFind) {
        return indexOf((byte[])array, (byte)valueToFind) != -1;
    }

    public static int indexOf(double[] array, double valueToFind) {
        return indexOf(array, valueToFind, 0);
    }

    public static int indexOf(double[] array, double valueToFind, double tolerance) {
        return indexOf(array, valueToFind, 0, tolerance);
    }

    public static int indexOf(double[] array, double valueToFind, int startIndex) {
        if(isEmpty((double[])array)) {
            return -1;
        } else {
            if(startIndex < 0) {
                startIndex = 0;
            }

            for(int i = startIndex; i < array.length; ++i) {
                if(valueToFind == array[i]) {
                    return i;
                }
            }

            return -1;
        }
    }

    public static int indexOf(double[] array, double valueToFind, int startIndex, double tolerance) {
        if(isEmpty((double[])array)) {
            return -1;
        } else {
            if(startIndex < 0) {
                startIndex = 0;
            }

            double min = valueToFind - tolerance;
            double max = valueToFind + tolerance;

            for(int i = startIndex; i < array.length; ++i) {
                if(array[i] >= min && array[i] <= max) {
                    return i;
                }
            }

            return -1;
        }
    }

    public static int lastIndexOf(double[] array, double valueToFind) {
        return lastIndexOf(array, valueToFind, 2147483647);
    }

    public static int lastIndexOf(double[] array, double valueToFind, double tolerance) {
        return lastIndexOf(array, valueToFind, 2147483647, tolerance);
    }

    public static int lastIndexOf(double[] array, double valueToFind, int startIndex) {
        if(isEmpty((double[])array)) {
            return -1;
        } else if(startIndex < 0) {
            return -1;
        } else {
            if(startIndex >= array.length) {
                startIndex = array.length - 1;
            }

            for(int i = startIndex; i >= 0; --i) {
                if(valueToFind == array[i]) {
                    return i;
                }
            }

            return -1;
        }
    }

    public static int lastIndexOf(double[] array, double valueToFind, int startIndex, double tolerance) {
        if(isEmpty((double[])array)) {
            return -1;
        } else if(startIndex < 0) {
            return -1;
        } else {
            if(startIndex >= array.length) {
                startIndex = array.length - 1;
            }

            double min = valueToFind - tolerance;
            double max = valueToFind + tolerance;

            for(int i = startIndex; i >= 0; --i) {
                if(array[i] >= min && array[i] <= max) {
                    return i;
                }
            }

            return -1;
        }
    }

    public static boolean contains(double[] array, double valueToFind) {
        return indexOf(array, valueToFind) != -1;
    }

    public static boolean contains(double[] array, double valueToFind, double tolerance) {
        return indexOf(array, valueToFind, 0, tolerance) != -1;
    }

    public static int indexOf(float[] array, float valueToFind) {
        return indexOf(array, valueToFind, 0);
    }

    public static int indexOf(float[] array, float valueToFind, int startIndex) {
        if(isEmpty((float[])array)) {
            return -1;
        } else {
            if(startIndex < 0) {
                startIndex = 0;
            }

            for(int i = startIndex; i < array.length; ++i) {
                if(valueToFind == array[i]) {
                    return i;
                }
            }

            return -1;
        }
    }

    public static int lastIndexOf(float[] array, float valueToFind) {
        return lastIndexOf(array, valueToFind, 2147483647);
    }

    public static int lastIndexOf(float[] array, float valueToFind, int startIndex) {
        if(isEmpty((float[])array)) {
            return -1;
        } else if(startIndex < 0) {
            return -1;
        } else {
            if(startIndex >= array.length) {
                startIndex = array.length - 1;
            }

            for(int i = startIndex; i >= 0; --i) {
                if(valueToFind == array[i]) {
                    return i;
                }
            }

            return -1;
        }
    }

    public static boolean contains(float[] array, float valueToFind) {
        return indexOf(array, valueToFind) != -1;
    }

    public static int indexOf(boolean[] array, boolean valueToFind) {
        return indexOf(array, valueToFind, 0);
    }

    public static int indexOf(boolean[] array, boolean valueToFind, int startIndex) {
        if(isEmpty((boolean[])array)) {
            return -1;
        } else {
            if(startIndex < 0) {
                startIndex = 0;
            }

            for(int i = startIndex; i < array.length; ++i) {
                if(valueToFind == array[i]) {
                    return i;
                }
            }

            return -1;
        }
    }

    public static int lastIndexOf(boolean[] array, boolean valueToFind) {
        return lastIndexOf(array, valueToFind, 2147483647);
    }

    public static int lastIndexOf(boolean[] array, boolean valueToFind, int startIndex) {
        if(isEmpty((boolean[])array)) {
            return -1;
        } else if(startIndex < 0) {
            return -1;
        } else {
            if(startIndex >= array.length) {
                startIndex = array.length - 1;
            }

            for(int i = startIndex; i >= 0; --i) {
                if(valueToFind == array[i]) {
                    return i;
                }
            }

            return -1;
        }
    }

    public static boolean contains(boolean[] array, boolean valueToFind) {
        return indexOf(array, valueToFind) != -1;
    }

    public static char[] toPrimitive(Character[] array) {
        if(array == null) {
            return null;
        } else if(array.length == 0) {
            return EMPTY_CHAR_ARRAY;
        } else {
            char[] result = new char[array.length];

            for(int i = 0; i < array.length; ++i) {
                result[i] = array[i].charValue();
            }

            return result;
        }
    }

    public static char[] toPrimitive(Character[] array, char valueForNull) {
        if(array == null) {
            return null;
        } else if(array.length == 0) {
            return EMPTY_CHAR_ARRAY;
        } else {
            char[] result = new char[array.length];

            for(int i = 0; i < array.length; ++i) {
                Character b = array[i];
                result[i] = b == null?valueForNull:b.charValue();
            }

            return result;
        }
    }

    public static Character[] toObject(char[] array) {
        if(array == null) {
            return null;
        } else if(array.length == 0) {
            return EMPTY_CHARACTER_OBJECT_ARRAY;
        } else {
            Character[] result = new Character[array.length];

            for(int i = 0; i < array.length; ++i) {
                result[i] = Character.valueOf(array[i]);
            }

            return result;
        }
    }

    public static long[] toPrimitive(Long[] array) {
        if(array == null) {
            return null;
        } else if(array.length == 0) {
            return EMPTY_LONG_ARRAY;
        } else {
            long[] result = new long[array.length];

            for(int i = 0; i < array.length; ++i) {
                result[i] = array[i].longValue();
            }

            return result;
        }
    }

    public static long[] toPrimitive(Long[] array, long valueForNull) {
        if(array == null) {
            return null;
        } else if(array.length == 0) {
            return EMPTY_LONG_ARRAY;
        } else {
            long[] result = new long[array.length];

            for(int i = 0; i < array.length; ++i) {
                Long b = array[i];
                result[i] = b == null?valueForNull:b.longValue();
            }

            return result;
        }
    }

    public static Long[] toObject(long[] array) {
        if(array == null) {
            return null;
        } else if(array.length == 0) {
            return EMPTY_LONG_OBJECT_ARRAY;
        } else {
            Long[] result = new Long[array.length];

            for(int i = 0; i < array.length; ++i) {
                result[i] = Long.valueOf(array[i]);
            }

            return result;
        }
    }

    public static int[] toPrimitive(Integer[] array) {
        if(array == null) {
            return null;
        } else if(array.length == 0) {
            return EMPTY_INT_ARRAY;
        } else {
            int[] result = new int[array.length];

            for(int i = 0; i < array.length; ++i) {
                result[i] = array[i].intValue();
            }

            return result;
        }
    }

    public static int[] toPrimitive(Integer[] array, int valueForNull) {
        if(array == null) {
            return null;
        } else if(array.length == 0) {
            return EMPTY_INT_ARRAY;
        } else {
            int[] result = new int[array.length];

            for(int i = 0; i < array.length; ++i) {
                Integer b = array[i];
                result[i] = b == null?valueForNull:b.intValue();
            }

            return result;
        }
    }

    public static Integer[] toObject(int[] array) {
        if(array == null) {
            return null;
        } else if(array.length == 0) {
            return EMPTY_INTEGER_OBJECT_ARRAY;
        } else {
            Integer[] result = new Integer[array.length];

            for(int i = 0; i < array.length; ++i) {
                result[i] = Integer.valueOf(array[i]);
            }

            return result;
        }
    }

    public static short[] toPrimitive(Short[] array) {
        if(array == null) {
            return null;
        } else if(array.length == 0) {
            return EMPTY_SHORT_ARRAY;
        } else {
            short[] result = new short[array.length];

            for(int i = 0; i < array.length; ++i) {
                result[i] = array[i].shortValue();
            }

            return result;
        }
    }

    public static short[] toPrimitive(Short[] array, short valueForNull) {
        if(array == null) {
            return null;
        } else if(array.length == 0) {
            return EMPTY_SHORT_ARRAY;
        } else {
            short[] result = new short[array.length];

            for(int i = 0; i < array.length; ++i) {
                Short b = array[i];
                result[i] = b == null?valueForNull:b.shortValue();
            }

            return result;
        }
    }

    public static Short[] toObject(short[] array) {
        if(array == null) {
            return null;
        } else if(array.length == 0) {
            return EMPTY_SHORT_OBJECT_ARRAY;
        } else {
            Short[] result = new Short[array.length];

            for(int i = 0; i < array.length; ++i) {
                result[i] = Short.valueOf(array[i]);
            }

            return result;
        }
    }

    public static byte[] toPrimitive(Byte[] array) {
        if(array == null) {
            return null;
        } else if(array.length == 0) {
            return EMPTY_BYTE_ARRAY;
        } else {
            byte[] result = new byte[array.length];

            for(int i = 0; i < array.length; ++i) {
                result[i] = array[i].byteValue();
            }

            return result;
        }
    }

    public static byte[] toPrimitive(Byte[] array, byte valueForNull) {
        if(array == null) {
            return null;
        } else if(array.length == 0) {
            return EMPTY_BYTE_ARRAY;
        } else {
            byte[] result = new byte[array.length];

            for(int i = 0; i < array.length; ++i) {
                Byte b = array[i];
                result[i] = b == null?valueForNull:b.byteValue();
            }

            return result;
        }
    }

    public static Byte[] toObject(byte[] array) {
        if(array == null) {
            return null;
        } else if(array.length == 0) {
            return EMPTY_BYTE_OBJECT_ARRAY;
        } else {
            Byte[] result = new Byte[array.length];

            for(int i = 0; i < array.length; ++i) {
                result[i] = Byte.valueOf(array[i]);
            }

            return result;
        }
    }

    public static double[] toPrimitive(Double[] array) {
        if(array == null) {
            return null;
        } else if(array.length == 0) {
            return EMPTY_DOUBLE_ARRAY;
        } else {
            double[] result = new double[array.length];

            for(int i = 0; i < array.length; ++i) {
                result[i] = array[i].doubleValue();
            }

            return result;
        }
    }

    public static double[] toPrimitive(Double[] array, double valueForNull) {
        if(array == null) {
            return null;
        } else if(array.length == 0) {
            return EMPTY_DOUBLE_ARRAY;
        } else {
            double[] result = new double[array.length];

            for(int i = 0; i < array.length; ++i) {
                Double b = array[i];
                result[i] = b == null?valueForNull:b.doubleValue();
            }

            return result;
        }
    }

    public static Double[] toObject(double[] array) {
        if(array == null) {
            return null;
        } else if(array.length == 0) {
            return EMPTY_DOUBLE_OBJECT_ARRAY;
        } else {
            Double[] result = new Double[array.length];

            for(int i = 0; i < array.length; ++i) {
                result[i] = Double.valueOf(array[i]);
            }

            return result;
        }
    }

    public static float[] toPrimitive(Float[] array) {
        if(array == null) {
            return null;
        } else if(array.length == 0) {
            return EMPTY_FLOAT_ARRAY;
        } else {
            float[] result = new float[array.length];

            for(int i = 0; i < array.length; ++i) {
                result[i] = array[i].floatValue();
            }

            return result;
        }
    }

    public static float[] toPrimitive(Float[] array, float valueForNull) {
        if(array == null) {
            return null;
        } else if(array.length == 0) {
            return EMPTY_FLOAT_ARRAY;
        } else {
            float[] result = new float[array.length];

            for(int i = 0; i < array.length; ++i) {
                Float b = array[i];
                result[i] = b == null?valueForNull:b.floatValue();
            }

            return result;
        }
    }

    public static Float[] toObject(float[] array) {
        if(array == null) {
            return null;
        } else if(array.length == 0) {
            return EMPTY_FLOAT_OBJECT_ARRAY;
        } else {
            Float[] result = new Float[array.length];

            for(int i = 0; i < array.length; ++i) {
                result[i] = Float.valueOf(array[i]);
            }

            return result;
        }
    }

    public static boolean[] toPrimitive(Boolean[] array) {
        if(array == null) {
            return null;
        } else if(array.length == 0) {
            return EMPTY_BOOLEAN_ARRAY;
        } else {
            boolean[] result = new boolean[array.length];

            for(int i = 0; i < array.length; ++i) {
                result[i] = array[i].booleanValue();
            }

            return result;
        }
    }

    public static boolean[] toPrimitive(Boolean[] array, boolean valueForNull) {
        if(array == null) {
            return null;
        } else if(array.length == 0) {
            return EMPTY_BOOLEAN_ARRAY;
        } else {
            boolean[] result = new boolean[array.length];

            for(int i = 0; i < array.length; ++i) {
                Boolean b = array[i];
                result[i] = b == null?valueForNull:b.booleanValue();
            }

            return result;
        }
    }

    public static Boolean[] toObject(boolean[] array) {
        if(array == null) {
            return null;
        } else if(array.length == 0) {
            return EMPTY_BOOLEAN_OBJECT_ARRAY;
        } else {
            Boolean[] result = new Boolean[array.length];

            for(int i = 0; i < array.length; ++i) {
                result[i] = array[i]?Boolean.TRUE:Boolean.FALSE;
            }

            return result;
        }
    }

    public static boolean isEmpty(Object[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isEmpty(long[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isEmpty(int[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isEmpty(short[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isEmpty(char[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isEmpty(byte[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isEmpty(double[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isEmpty(float[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isEmpty(boolean[] array) {
        return array == null || array.length == 0;
    }

    public static <T> boolean isNotEmpty(T[] array) {
        return array != null && array.length != 0;
    }

    public static boolean isNotEmpty(long[] array) {
        return array != null && array.length != 0;
    }

    public static boolean isNotEmpty(int[] array) {
        return array != null && array.length != 0;
    }

    public static boolean isNotEmpty(short[] array) {
        return array != null && array.length != 0;
    }

    public static boolean isNotEmpty(char[] array) {
        return array != null && array.length != 0;
    }

    public static boolean isNotEmpty(byte[] array) {
        return array != null && array.length != 0;
    }

    public static boolean isNotEmpty(double[] array) {
        return array != null && array.length != 0;
    }

    public static boolean isNotEmpty(float[] array) {
        return array != null && array.length != 0;
    }

    public static boolean isNotEmpty(boolean[] array) {
        return array != null && array.length != 0;
    }

	public static <T> T[] addAll(T[] array1, T... array2) {
        if(array1 == null) {
            return (T[]) clone((Object[])array2);
        } else if(array2 == null) {
            return (T[]) clone((Object[])array1);
        } else {
            Class<?> type1 = array1.getClass().getComponentType();
            Object[] joinedArray = (Object[])((Object[])Array.newInstance(type1, array1.length + array2.length));
            System.arraycopy(array1, 0, joinedArray, 0, array1.length);

            try {
                System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
                return (T[]) joinedArray;
            } catch (ArrayStoreException var6) {
                Class<?> type2 = array2.getClass().getComponentType();
                if(!type1.isAssignableFrom(type2)) {
                    throw new IllegalArgumentException("Cannot store " + type2.getName() + " in an array of " + type1.getName(), var6);
                } else {
                    throw var6;
                }
            }
        }
    }

    public static boolean[] addAll(boolean[] array1, boolean... array2) {
        if(array1 == null) {
            return clone((boolean[])array2);
        } else if(array2 == null) {
            return clone((boolean[])array1);
        } else {
            boolean[] joinedArray = new boolean[array1.length + array2.length];
            System.arraycopy(array1, 0, joinedArray, 0, array1.length);
            System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
            return joinedArray;
        }
    }

    public static char[] addAll(char[] array1, char... array2) {
        if(array1 == null) {
            return clone((char[])array2);
        } else if(array2 == null) {
            return clone((char[])array1);
        } else {
            char[] joinedArray = new char[array1.length + array2.length];
            System.arraycopy(array1, 0, joinedArray, 0, array1.length);
            System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
            return joinedArray;
        }
    }

    public static byte[] addAll(byte[] array1, byte... array2) {
        if(array1 == null) {
            return clone((byte[])array2);
        } else if(array2 == null) {
            return clone((byte[])array1);
        } else {
            byte[] joinedArray = new byte[array1.length + array2.length];
            System.arraycopy(array1, 0, joinedArray, 0, array1.length);
            System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
            return joinedArray;
        }
    }

    public static short[] addAll(short[] array1, short... array2) {
        if(array1 == null) {
            return clone((short[])array2);
        } else if(array2 == null) {
            return clone((short[])array1);
        } else {
            short[] joinedArray = new short[array1.length + array2.length];
            System.arraycopy(array1, 0, joinedArray, 0, array1.length);
            System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
            return joinedArray;
        }
    }

    public static int[] addAll(int[] array1, int... array2) {
        if(array1 == null) {
            return clone((int[])array2);
        } else if(array2 == null) {
            return clone((int[])array1);
        } else {
            int[] joinedArray = new int[array1.length + array2.length];
            System.arraycopy(array1, 0, joinedArray, 0, array1.length);
            System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
            return joinedArray;
        }
    }

    public static long[] addAll(long[] array1, long... array2) {
        if(array1 == null) {
            return clone((long[])array2);
        } else if(array2 == null) {
            return clone((long[])array1);
        } else {
            long[] joinedArray = new long[array1.length + array2.length];
            System.arraycopy(array1, 0, joinedArray, 0, array1.length);
            System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
            return joinedArray;
        }
    }

    public static float[] addAll(float[] array1, float... array2) {
        if(array1 == null) {
            return clone((float[])array2);
        } else if(array2 == null) {
            return clone((float[])array1);
        } else {
            float[] joinedArray = new float[array1.length + array2.length];
            System.arraycopy(array1, 0, joinedArray, 0, array1.length);
            System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
            return joinedArray;
        }
    }

    public static double[] addAll(double[] array1, double... array2) {
        if(array1 == null) {
            return clone((double[])array2);
        } else if(array2 == null) {
            return clone((double[])array1);
        } else {
            double[] joinedArray = new double[array1.length + array2.length];
            System.arraycopy(array1, 0, joinedArray, 0, array1.length);
            System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
            return joinedArray;
        }
    }

    public static <T> T[] add(T[] array, T element) {
        Class<?> type;
        if(array != null) {
            type = array.getClass();
        } else {
            if(element == null) {
                throw new IllegalArgumentException("Arguments cannot both be null");
            }

            type = element.getClass();
        }

        Object[] newArray = (Object[])((Object[])copyArrayGrow1(array, type));
        newArray[newArray.length - 1] = element;
        return (T[]) newArray;
    }

    public static boolean[] add(boolean[] array, boolean element) {
        boolean[] newArray = (boolean[])((boolean[])copyArrayGrow1(array, Boolean.TYPE));
        newArray[newArray.length - 1] = element;
        return newArray;
    }

    public static byte[] add(byte[] array, byte element) {
        byte[] newArray = (byte[])((byte[])copyArrayGrow1(array, Byte.TYPE));
        newArray[newArray.length - 1] = element;
        return newArray;
    }

    public static char[] add(char[] array, char element) {
        char[] newArray = (char[])((char[])copyArrayGrow1(array, Character.TYPE));
        newArray[newArray.length - 1] = element;
        return newArray;
    }

    public static double[] add(double[] array, double element) {
        double[] newArray = (double[])((double[])copyArrayGrow1(array, Double.TYPE));
        newArray[newArray.length - 1] = element;
        return newArray;
    }

    public static float[] add(float[] array, float element) {
        float[] newArray = (float[])((float[])copyArrayGrow1(array, Float.TYPE));
        newArray[newArray.length - 1] = element;
        return newArray;
    }

    public static int[] add(int[] array, int element) {
        int[] newArray = (int[])((int[])copyArrayGrow1(array, Integer.TYPE));
        newArray[newArray.length - 1] = element;
        return newArray;
    }

    public static long[] add(long[] array, long element) {
        long[] newArray = (long[])((long[])copyArrayGrow1(array, Long.TYPE));
        newArray[newArray.length - 1] = element;
        return newArray;
    }

    public static short[] add(short[] array, short element) {
        short[] newArray = (short[])((short[])copyArrayGrow1(array, Short.TYPE));
        newArray[newArray.length - 1] = element;
        return newArray;
    }

    private static Object copyArrayGrow1(Object array, Class<?> newArrayComponentType) {
        if(array != null) {
            int arrayLength = Array.getLength(array);
            Object newArray = Array.newInstance(array.getClass().getComponentType(), arrayLength + 1);
            System.arraycopy(array, 0, newArray, 0, arrayLength);
            return newArray;
        } else {
            return Array.newInstance(newArrayComponentType, 1);
        }
    }

    public static <T> T[] add(T[] array, int index, T element) {
        Class<?> clss = null;
        if(array != null) {
            clss = array.getClass().getComponentType();
        } else {
            if(element == null) {
                throw new IllegalArgumentException("Array and element cannot both be null");
            }

            clss = element.getClass();
        }

        Object[] newArray = (Object[])((Object[])add(array, index, element, clss));
        return (T[]) newArray;
    }

    public static boolean[] add(boolean[] array, int index, boolean element) {
        return (boolean[])((boolean[])add(array, index, Boolean.valueOf(element), Boolean.TYPE));
    }

    public static char[] add(char[] array, int index, char element) {
        return (char[])((char[])add(array, index, Character.valueOf(element), Character.TYPE));
    }

    public static byte[] add(byte[] array, int index, byte element) {
        return (byte[])((byte[])add(array, index, Byte.valueOf(element), Byte.TYPE));
    }

    public static short[] add(short[] array, int index, short element) {
        return (short[])((short[])add(array, index, Short.valueOf(element), Short.TYPE));
    }

    public static int[] add(int[] array, int index, int element) {
        return (int[])((int[])add(array, index, Integer.valueOf(element), Integer.TYPE));
    }

    public static long[] add(long[] array, int index, long element) {
        return (long[])((long[])add(array, index, Long.valueOf(element), Long.TYPE));
    }

    public static float[] add(float[] array, int index, float element) {
        return (float[])((float[])add(array, index, Float.valueOf(element), Float.TYPE));
    }

    public static double[] add(double[] array, int index, double element) {
        return (double[])((double[])add(array, index, Double.valueOf(element), Double.TYPE));
    }

    private static Object add(Object array, int index, Object element, Class<?> clss) {
        if(array == null) {
            if(index != 0) {
                throw new IndexOutOfBoundsException("Index: " + index + ", Length: 0");
            } else {
                Object length1 = Array.newInstance(clss, 1);
                Array.set(length1, 0, element);
                return length1;
            }
        } else {
            int length = Array.getLength(array);
            if(index <= length && index >= 0) {
                Object result = Array.newInstance(clss, length + 1);
                System.arraycopy(array, 0, result, 0, index);
                Array.set(result, index, element);
                if(index < length) {
                    System.arraycopy(array, index, result, index + 1, length - index);
                }

                return result;
            } else {
                throw new IndexOutOfBoundsException("Index: " + index + ", Length: " + length);
            }
        }
    }

    public static <T> T[] remove(T[] array, int index) {
        return (T[])((Object[])remove((Object)array, index));
    }

    public static <T> T[] removeElement(T[] array, Object element) {
        int index = indexOf(array, element);
        return (T[]) (index == -1?clone((Object[])array):remove((Object[])array, index));
    }

    public static boolean[] remove(boolean[] array, int index) {
        return (boolean[])((boolean[])remove((Object)array, index));
    }

    public static boolean[] removeElement(boolean[] array, boolean element) {
        int index = indexOf(array, element);
        return index == -1?clone((boolean[])array):remove((boolean[])array, index);
    }

    public static byte[] remove(byte[] array, int index) {
        return (byte[])((byte[])remove((Object)array, index));
    }

    public static byte[] removeElement(byte[] array, byte element) {
        int index = indexOf((byte[])array, (byte)element);
        return index == -1?clone((byte[])array):remove((byte[])array, index);
    }

    public static char[] remove(char[] array, int index) {
        return (char[])((char[])remove((Object)array, index));
    }

    public static char[] removeElement(char[] array, char element) {
        int index = indexOf((char[])array, (char)element);
        return index == -1?clone((char[])array):remove((char[])array, index);
    }

    public static double[] remove(double[] array, int index) {
        return (double[])((double[])remove((Object)array, index));
    }

    public static double[] removeElement(double[] array, double element) {
        int index = indexOf(array, element);
        return index == -1?clone((double[])array):remove((double[])array, index);
    }

    public static float[] remove(float[] array, int index) {
        return (float[])((float[])remove((Object)array, index));
    }

    public static float[] removeElement(float[] array, float element) {
        int index = indexOf(array, element);
        return index == -1?clone((float[])array):remove((float[])array, index);
    }

    public static int[] remove(int[] array, int index) {
        return (int[])((int[])remove((Object)array, index));
    }

    public static int[] removeElement(int[] array, int element) {
        int index = indexOf((int[])array, (int)element);
        return index == -1?clone((int[])array):remove((int[])array, index);
    }

    public static long[] remove(long[] array, int index) {
        return (long[])((long[])remove((Object)array, index));
    }

    public static long[] removeElement(long[] array, long element) {
        int index = indexOf(array, element);
        return index == -1?clone((long[])array):remove((long[])array, index);
    }

    public static short[] remove(short[] array, int index) {
        return (short[])((short[])remove((Object)array, index));
    }

    public static short[] removeElement(short[] array, short element) {
        int index = indexOf((short[])array, (short)element);
        return index == -1?clone((short[])array):remove((short[])array, index);
    }

    private static Object remove(Object array, int index) {
        int length = getLength(array);
        if(index >= 0 && index < length) {
            Object result = Array.newInstance(array.getClass().getComponentType(), length - 1);
            System.arraycopy(array, 0, result, 0, index);
            if(index < length - 1) {
                System.arraycopy(array, index + 1, result, index, length - index - 1);
            }

            return result;
        } else {
            throw new IndexOutOfBoundsException("Index: " + index + ", Length: " + length);
        }
    }

    public static <T> T[] removeAll(T[] array, int... indices) {
        return (T[])((Object[])removeAll((Object)array, (int[])clone((int[])indices)));
    }

    public static <T> T[] removeElements(T[] array, T... values) {
        if(!isEmpty((Object[])array) && !isEmpty((Object[])values)) {
            HashMap<Object, MutableInt> occurrences = new HashMap<Object, MutableInt>(values.length);
            Object[] toRemove = values;
            int result = values.length;

            Object v;
            for(int e = 0; e < result; ++e) {
                v = toRemove[e];
                MutableInt found = (MutableInt)occurrences.get(v);
                if(found == null) {
                    occurrences.put(v, new MutableInt(1));
                } else {
                    found.increment();
                }
            }

            BitSet var10 = new BitSet();
            Iterator<?> var11 = occurrences.entrySet().iterator();

            while(var11.hasNext()) {
                Entry<?, ?> var13 = (Entry<?, ?>)var11.next();
                v = var13.getKey();
                int var14 = 0;
                int i = 0;

                for(int ct = ((MutableInt)var13.getValue()).intValue(); i < ct; ++i) {
                    var14 = indexOf(array, v, var14);
                    if(var14 < 0) {
                        break;
                    }

                    var10.set(var14++);
                }
            }

            Object[] var12 = (Object[])((Object[])removeAll((Object)array, (BitSet)var10));
            return (T[]) var12;
        } else {
            return (T[]) clone((Object[])array);
        }
    }

    public static byte[] removeAll(byte[] array, int... indices) {
        return (byte[])((byte[])removeAll((Object)array, (int[])clone((int[])indices)));
    }

    public static byte[] removeElements(byte[] array, byte... values) {
        if(!isEmpty((byte[])array) && !isEmpty((byte[])values)) {
            HashMap<Byte, MutableInt> occurrences = new HashMap<Byte, MutableInt>(values.length);
            byte[] toRemove = values;
            int i$ = values.length;

            for(int e = 0; e < i$; ++e) {
                byte v = toRemove[e];
                Byte found = Byte.valueOf(v);
                MutableInt i = (MutableInt)occurrences.get(found);
                if(i == null) {
                    occurrences.put(found, new MutableInt(1));
                } else {
                    i.increment();
                }
            }

            BitSet var10 = new BitSet();
            Iterator<?> var11 = occurrences.entrySet().iterator();

            while(var11.hasNext()) {
                Entry<?, ?> var12 = (Entry<?, ?>)var11.next();
                Byte var13 = (Byte)var12.getKey();
                int var14 = 0;
                int var15 = 0;

                for(int ct = ((MutableInt)var12.getValue()).intValue(); var15 < ct; ++var15) {
                    var14 = indexOf((byte[])array, (byte)var13.byteValue(), var14);
                    if(var14 < 0) {
                        break;
                    }

                    var10.set(var14++);
                }
            }

            return (byte[])((byte[])removeAll((Object)array, (BitSet)var10));
        } else {
            return clone((byte[])array);
        }
    }

    public static short[] removeAll(short[] array, int... indices) {
        return (short[])((short[])removeAll((Object)array, (int[])clone((int[])indices)));
    }

    public static short[] removeElements(short[] array, short... values) {
        if(!isEmpty((short[])array) && !isEmpty((short[])values)) {
            HashMap<Short, MutableInt> occurrences = new HashMap<Short, MutableInt>(values.length);
            short[] toRemove = values;
            int i$ = values.length;

            for(int e = 0; e < i$; ++e) {
                short v = toRemove[e];
                Short found = Short.valueOf(v);
                MutableInt i = (MutableInt)occurrences.get(found);
                if(i == null) {
                    occurrences.put(found, new MutableInt(1));
                } else {
                    i.increment();
                }
            }

            BitSet var10 = new BitSet();
            Iterator<?> var11 = occurrences.entrySet().iterator();

            while(var11.hasNext()) {
                Entry<?, ?> var12 = (Entry<?, ?>)var11.next();
                Short var13 = (Short)var12.getKey();
                int var14 = 0;
                int var15 = 0;

                for(int ct = ((MutableInt)var12.getValue()).intValue(); var15 < ct; ++var15) {
                    var14 = indexOf((short[])array, (short)var13.shortValue(), var14);
                    if(var14 < 0) {
                        break;
                    }

                    var10.set(var14++);
                }
            }

            return (short[])((short[])removeAll((Object)array, (BitSet)var10));
        } else {
            return clone((short[])array);
        }
    }

    public static int[] removeAll(int[] array, int... indices) {
        return (int[])((int[])removeAll((Object)array, (int[])clone((int[])indices)));
    }

    public static int[] removeElements(int[] array, int... values) {
        if(!isEmpty((int[])array) && !isEmpty((int[])values)) {
            HashMap<Integer, MutableInt> occurrences = new HashMap<Integer, MutableInt>(values.length);
            int[] toRemove = values;
            int i$ = values.length;

            for(int e = 0; e < i$; ++e) {
                int v = toRemove[e];
                Integer found = Integer.valueOf(v);
                MutableInt i = (MutableInt)occurrences.get(found);
                if(i == null) {
                    occurrences.put(found, new MutableInt(1));
                } else {
                    i.increment();
                }
            }

            BitSet var10 = new BitSet();
            Iterator<?> var11 = occurrences.entrySet().iterator();

            while(var11.hasNext()) {
                Entry<?, ?> var12 = (Entry<?, ?>)var11.next();
                Integer var13 = (Integer)var12.getKey();
                int var14 = 0;
                int var15 = 0;

                for(int ct = ((MutableInt)var12.getValue()).intValue(); var15 < ct; ++var15) {
                    var14 = indexOf((int[])array, (int)var13.intValue(), var14);
                    if(var14 < 0) {
                        break;
                    }

                    var10.set(var14++);
                }
            }

            return (int[])((int[])removeAll((Object)array, (BitSet)var10));
        } else {
            return clone((int[])array);
        }
    }

    public static char[] removeAll(char[] array, int... indices) {
        return (char[])((char[])removeAll((Object)array, (int[])clone((int[])indices)));
    }

    public static char[] removeElements(char[] array, char... values) {
        if(!isEmpty((char[])array) && !isEmpty((char[])values)) {
            HashMap<Character, MutableInt> occurrences = new HashMap<Character, MutableInt>(values.length);
            char[] toRemove = values;
            int i$ = values.length;

            for(int e = 0; e < i$; ++e) {
                char v = toRemove[e];
                Character found = Character.valueOf(v);
                MutableInt i = (MutableInt)occurrences.get(found);
                if(i == null) {
                    occurrences.put(found, new MutableInt(1));
                } else {
                    i.increment();
                }
            }

            BitSet var10 = new BitSet();
            Iterator<?> var11 = occurrences.entrySet().iterator();

            while(var11.hasNext()) {
                Entry<?, ?> var12 = (Entry<?, ?>)var11.next();
                Character var13 = (Character)var12.getKey();
                int var14 = 0;
                int var15 = 0;

                for(int ct = ((MutableInt)var12.getValue()).intValue(); var15 < ct; ++var15) {
                    var14 = indexOf((char[])array, (char)var13.charValue(), var14);
                    if(var14 < 0) {
                        break;
                    }

                    var10.set(var14++);
                }
            }

            return (char[])((char[])removeAll((Object)array, (BitSet)var10));
        } else {
            return clone((char[])array);
        }
    }

    public static long[] removeAll(long[] array, int... indices) {
        return (long[])((long[])removeAll((Object)array, (int[])clone((int[])indices)));
    }

    public static long[] removeElements(long[] array, long... values) {
        if(!isEmpty((long[])array) && !isEmpty((long[])values)) {
            HashMap<Long, MutableInt> occurrences = new HashMap<Long, MutableInt>(values.length);
            long[] toRemove = values;
            int i$ = values.length;

            for(int e = 0; e < i$; ++e) {
                long v = toRemove[e];
                Long i = Long.valueOf(v);
                MutableInt ct = (MutableInt)occurrences.get(i);
                if(ct == null) {
                    occurrences.put(i, new MutableInt(1));
                } else {
                    ct.increment();
                }
            }

            BitSet var10 = new BitSet();
            Iterator<?> var11 = occurrences.entrySet().iterator();

            while(var11.hasNext()) {
                Entry<?, ?> var12 = (Entry<?, ?>)var11.next();
                Long var13 = (Long)var12.getKey();
                int found = 0;
                int var14 = 0;

                for(int var15 = ((MutableInt)var12.getValue()).intValue(); var14 < var15; ++var14) {
                    found = indexOf(array, var13.longValue(), found);
                    if(found < 0) {
                        break;
                    }

                    var10.set(found++);
                }
            }

            return (long[])((long[])removeAll((Object)array, (BitSet)var10));
        } else {
            return clone((long[])array);
        }
    }

    public static float[] removeAll(float[] array, int... indices) {
        return (float[])((float[])removeAll((Object)array, (int[])clone((int[])indices)));
    }

    public static float[] removeElements(float[] array, float... values) {
        if(!isEmpty((float[])array) && !isEmpty((float[])values)) {
            HashMap<Float, MutableInt> occurrences = new HashMap<Float, MutableInt>(values.length);
            float[] toRemove = values;
            int i$ = values.length;

            for(int e = 0; e < i$; ++e) {
                float v = toRemove[e];
                Float found = Float.valueOf(v);
                MutableInt i = (MutableInt)occurrences.get(found);
                if(i == null) {
                    occurrences.put(found, new MutableInt(1));
                } else {
                    i.increment();
                }
            }

            BitSet var10 = new BitSet();
            Iterator<?> var11 = occurrences.entrySet().iterator();

            while(var11.hasNext()) {
                Entry<?, ?> var12 = (Entry<?, ?>)var11.next();
                Float var13 = (Float)var12.getKey();
                int var14 = 0;
                int var15 = 0;

                for(int ct = ((MutableInt)var12.getValue()).intValue(); var15 < ct; ++var15) {
                    var14 = indexOf(array, var13.floatValue(), var14);
                    if(var14 < 0) {
                        break;
                    }

                    var10.set(var14++);
                }
            }

            return (float[])((float[])removeAll((Object)array, (BitSet)var10));
        } else {
            return clone((float[])array);
        }
    }

    public static double[] removeAll(double[] array, int... indices) {
        return (double[])((double[])removeAll((Object)array, (int[])clone((int[])indices)));
    }

    public static double[] removeElements(double[] array, double... values) {
        if(!isEmpty((double[])array) && !isEmpty((double[])values)) {
            HashMap<Double, MutableInt> occurrences = new HashMap<Double, MutableInt>(values.length);
            double[] toRemove = values;
            int i$ = values.length;

            for(int e = 0; e < i$; ++e) {
                double v = toRemove[e];
                Double i = Double.valueOf(v);
                MutableInt ct = (MutableInt)occurrences.get(i);
                if(ct == null) {
                    occurrences.put(i, new MutableInt(1));
                } else {
                    ct.increment();
                }
            }

            BitSet var10 = new BitSet();
            Iterator<?> var11 = occurrences.entrySet().iterator();

            while(var11.hasNext()) {
                Entry<?, ?> var12 = (Entry<?, ?>)var11.next();
                Double var13 = (Double)var12.getKey();
                int found = 0;
                int var14 = 0;

                for(int var15 = ((MutableInt)var12.getValue()).intValue(); var14 < var15; ++var14) {
                    found = indexOf(array, var13.doubleValue(), found);
                    if(found < 0) {
                        break;
                    }

                    var10.set(found++);
                }
            }

            return (double[])((double[])removeAll((Object)array, (BitSet)var10));
        } else {
            return clone((double[])array);
        }
    }

    public static boolean[] removeAll(boolean[] array, int... indices) {
        return (boolean[])((boolean[])removeAll((Object)array, (int[])clone((int[])indices)));
    }

    public static boolean[] removeElements(boolean[] array, boolean... values) {
        if(!isEmpty((boolean[])array) && !isEmpty((boolean[])values)) {
            HashMap<Boolean, MutableInt> occurrences = new HashMap<Boolean, MutableInt>(2);
            boolean[] toRemove = values;
            int i$ = values.length;

            for(int e = 0; e < i$; ++e) {
                boolean v = toRemove[e];
                Boolean found = Boolean.valueOf(v);
                MutableInt i = (MutableInt)occurrences.get(found);
                if(i == null) {
                    occurrences.put(found, new MutableInt(1));
                } else {
                    i.increment();
                }
            }

            BitSet var10 = new BitSet();
            Iterator<?> var11 = occurrences.entrySet().iterator();

            while(var11.hasNext()) {
                Entry<?, ?> var12 = (Entry<?, ?>)var11.next();
                Boolean var13 = (Boolean)var12.getKey();
                int var14 = 0;
                int var15 = 0;

                for(int ct = ((MutableInt)var12.getValue()).intValue(); var15 < ct; ++var15) {
                    var14 = indexOf(array, var13.booleanValue(), var14);
                    if(var14 < 0) {
                        break;
                    }

                    var10.set(var14++);
                }
            }

            return (boolean[])((boolean[])removeAll((Object)array, (BitSet)var10));
        } else {
            return clone((boolean[])array);
        }
    }

    static Object removeAll(Object array, int... indices) {
        int length = getLength(array);
        int diff = 0;
        int end;
        int dest;
        if(isNotEmpty((int[])indices)) {
            Arrays.sort(indices);
            int result = indices.length;
            end = length;

            while(true) {
                --result;
                if(result < 0) {
                    break;
                }

                dest = indices[result];
                if(dest < 0 || dest >= length) {
                    throw new IndexOutOfBoundsException("Index: " + dest + ", Length: " + length);
                }

                if(dest < end) {
                    ++diff;
                    end = dest;
                }
            }
        }

        Object var10 = Array.newInstance(array.getClass().getComponentType(), length - diff);
        if(diff < length) {
            end = length;
            dest = length - diff;

            for(int i = indices.length - 1; i >= 0; --i) {
                int index = indices[i];
                if(end - index > 1) {
                    int cp = end - index - 1;
                    dest -= cp;
                    System.arraycopy(array, index + 1, var10, dest, cp);
                }

                end = index;
            }

            if(end > 0) {
                System.arraycopy(array, 0, var10, 0, end);
            }
        }

        return var10;
    }

    static Object removeAll(Object array, BitSet indices) {
        int srcLength = getLength(array);
        int removals = indices.cardinality();
        Object result = Array.newInstance(array.getClass().getComponentType(), srcLength - removals);
        int srcIndex = 0;

        int destIndex;
        int count;
        int set;
        for(destIndex = 0; (set = indices.nextSetBit(srcIndex)) != -1; srcIndex = indices.nextClearBit(set)) {
            count = set - srcIndex;
            if(count > 0) {
                System.arraycopy(array, srcIndex, result, destIndex, count);
                destIndex += count;
            }
        }

        count = srcLength - srcIndex;
        if(count > 0) {
            System.arraycopy(array, srcIndex, result, destIndex, count);
        }

        return result;
    }
}
