package de.sirati97.bex_proto.datahandler;

/**
 * Created by sirati97 on 13.05.2016.
 */
@SuppressWarnings("UnnecessaryUnboxing")
public class PrimitiveHelper {
    public static PrimitiveHelper INSTANCE = new PrimitiveHelper();
    protected PrimitiveHelper(){}

    public byte toByte(Object object) {
        return ((Byte) object).byteValue();
    }

    public short toShort(Object object) {
        return ((Short) object).shortValue();
    }

    public int toInt(Object object) {
        return ((Integer) object).intValue();
    }

    public long toLong(Object object) {
        return ((Long) object).longValue();
    }

    public double toDouble(Object object) {
        return ((Double) object).doubleValue();
    }

    public float toFloat(Object object) {
        return ((Float) object).floatValue();
    }

    public boolean toBoolean(Object object) {
        return ((Boolean) object).booleanValue();
    }

    public boolean isByte(Object object) {
        return object instanceof Byte;
    }

    public boolean isShort(Object object) {
        return object instanceof Short;
    }

    public boolean isInt(Object object) {
        return object instanceof Integer;
    }

    public boolean isLong(Object object) {
        return object instanceof Long;
    }

    public boolean isDouble(Object object) {
        return object instanceof Double;
    }

    public boolean isFloat(Object object) {
        return object instanceof Float;
    }

    public boolean isBoolean(Object object) {
        return object instanceof Boolean;
    }

    public boolean isPrimitive(Object object) {
        return isInt(object) || isBoolean(object) || isDouble(object) || isLong(object) || isFloat(object) || isShort(object) || isByte(object);
    }

    public boolean isByte(Class clazz) {
        return Byte.class.isAssignableFrom(clazz) || byte.class == clazz;
    }

    public boolean isShort(Class clazz) {
        return Short.class.isAssignableFrom(clazz) || short.class == clazz;
    }

    public boolean isInt(Class clazz) {
        return Integer.class.isAssignableFrom(clazz) || int.class == clazz;
    }

    public boolean isLong(Class clazz) {
        return Long.class.isAssignableFrom(clazz) || long.class == clazz;
    }

    public boolean isDouble(Class clazz) {
        return Double.class.isAssignableFrom(clazz) || double.class == clazz;
    }

    public boolean isFloat(Class clazz) {
        return Float.class.isAssignableFrom(clazz) || float.class == clazz;
    }

    public boolean isBoolean(Class clazz) {
        return Boolean.class.isAssignableFrom(clazz) || boolean.class == clazz;
    }

    public boolean isPrimitive(Class clazz) {
        return isInt(clazz) || isBoolean(clazz) || isDouble(clazz) || isLong(clazz) || isFloat(clazz) || isShort(clazz) || isByte(clazz);
    }
}
