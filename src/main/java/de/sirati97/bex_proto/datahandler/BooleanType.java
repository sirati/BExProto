package de.sirati97.bex_proto.datahandler;

import org.apache.commons.lang3.ArrayUtils;

/**
 * Created by sirati97 on 28.04.2016.
 */
public class BooleanType extends PrimitiveType<Boolean> {

    public BooleanType() {
        super(new BooleanEncoder(), new BooleanDecoder());
    }

    @Override
    public Object toPrimitiveArray(Boolean[] obj) {
        return ArrayUtils.toPrimitive(obj);
    }

    @Override
    public Boolean[] toObjectArray(Object obj) {
        return ArrayUtils.toObject((boolean[]) obj);
    }

    @Override
    public Boolean castToPrimitive(Object obj) {
        return PrimitiveHelper.INSTANCE.toBoolean(obj);
    }

    @Override
    public Class<?> getType() {
        return boolean.class;
    }

    @Override
    public Class<Boolean> getObjType() {
        return Boolean.class;
    }

    public String getTypeName() {
        return "Boolean";
    }

    @Override
    protected IArrayType<Boolean> createArrayType() {
        return new BitArrayType(this);
    }

    @Override
    public boolean isEncodable(Object obj, boolean platformIndependent) {
        return PrimitiveHelper.INSTANCE.isBoolean(obj);
    }

    @Override
    public boolean isEncodable(Class clazz, boolean platformIndependent) {
        return PrimitiveHelper.INSTANCE.isBoolean(clazz);
    }
}