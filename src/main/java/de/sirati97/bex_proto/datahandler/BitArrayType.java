package de.sirati97.bex_proto.datahandler;

/**
 * Created by sirati97 on 25.04.2016.
 */
public class BitArrayType extends ArrayType<Boolean> {
    private static short[] bitSetter = new short[] {1, 2, 4, 8, 16, 32, 64, 128};

    public static short getBitSetter(int bit) {
        return bitSetter[bit];
    }

    public BitArrayType(BooleanType type) {
        super(type);
    }

    @Override
    public Stream createStream(Object obj) {
        return new BitArrayStream((boolean[]) obj);
    }

    @Override
    protected IArrayType<Boolean[]> createArrayType() {
        return new ArrayType<>(this);
    }

    @Override
    protected StreamExtractor<Boolean[]> createExtractor() {
        return new BitArrayExtractor();
    }

}
