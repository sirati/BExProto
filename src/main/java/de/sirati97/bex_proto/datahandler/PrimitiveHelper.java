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
}
