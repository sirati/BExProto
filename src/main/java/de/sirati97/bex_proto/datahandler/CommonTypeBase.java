package de.sirati97.bex_proto.datahandler;

/**
 * Created by sirati97 on 29.01.2017 for BexProto.
 */
public abstract class CommonTypeBase<Type> implements IType<Type> {
    private INullableType<Type> nullableType;
    private IArrayType<Type> arrayType;
    private final IEncoder<Type> encoder;
    private final IDecoder<Type> decoder;
    
    public CommonTypeBase(IEncoder<Type> encoder, IDecoder<Type> decoder) {
        this.encoder = encoder;
        this.decoder = decoder;
    }

    protected IArrayType<Type> createArrayType() {
        return new ArrayType<>(this);
    }

    protected INullableType<Type> createNullableType() {
        return new NullableType<>(this);
    }

    @Override
    public final INullableType<Type> asNullable() {
        return nullableType==null?(nullableType=createNullableType()):nullableType;
    }

    @Override
    public final IArrayType<Type> asArray() {
        return arrayType==null?(arrayType=createArrayType()):arrayType;
    }

    @Override
    public final IEncoder<Type> getEncoder() {
        return encoder;
    }

    @Override
    public final IDecoder<Type> getDecoder() {
        return decoder;
    }
}
