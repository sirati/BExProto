package de.sirati97.bex_proto.datahandler;

/**
 * Created by sirati97 on 28.04.2016.
 */
public class ArrayNullableType<T> extends ArrayType<T> {
    public ArrayNullableType(INullableType<T> type) {
        super(type);
    }

    @Override
    public Stream createStream(Object obj) {
        return new ArrayNullableStream(getInnerType(), obj);
    }

    @Override
    public INullableType<T> getInnerType() {
        return (INullableType<T>) super.getInnerType();
    }

    @Override
    protected IArrayType<T[]> createArrayType() {
        return new ArrayType<>(this);
    }

    @Override
    protected StreamExtractor<T[]> createExtractor() {
        return new ArrayNullableExtractor<>(getInnerType());
    }
}
