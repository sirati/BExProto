package de.sirati97.bex_proto.datahandler;

/**
 * Created by sirati97 on 28.04.2016.
 */
public class ArrayNullableType<T> extends ArrayType<T> {
    public ArrayNullableType(INullableType<T> type) {
        super(new ArrayNullableEncoder<>(type), new ArrayNullableDecoder<>(type), type);
    }
    @Override
    public INullableType<T> getInnerType() {
        return (INullableType<T>) super.getInnerType();
    }
}
