package de.sirati97.bex_proto.datahandler;

import org.apache.commons.lang3.ArrayUtils;

/**
 * Created by sirati97 on 28.04.2016.
 */
public class BooleanType extends PrimitiveType<Boolean> {
    BooleanExtractor extractor = new BooleanExtractor();

    @Override
    public Stream createStreamCasted(Boolean obj) {
        return new BooleanStream(obj);
    }

    @Override
    public BooleanExtractor getExtractor() {
        return extractor;
    }

    @Override
    public Object toPrimitiveArray(Object obj) {
        return ArrayUtils.toPrimitive((Boolean[]) obj);
    }

    @Override
    public Object toObjectArray(Object obj) {
        return ArrayUtils.toObject((boolean[]) obj);
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
}