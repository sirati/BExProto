package de.sirati97.bex_proto.datahandler;

public abstract class PrimitiveType<WrapperType> extends Type<WrapperType> {

    public PrimitiveType(IEncoder<WrapperType> encoder, IDecoder<WrapperType> decoder) {
        super(encoder, decoder);
    }



    @Override public boolean isPrimitive() {return true;}
	public abstract Object toPrimitiveArray(WrapperType[] obj);
	public abstract WrapperType[] toObjectArray(Object obj);
	public abstract WrapperType castToPrimitive(Object obj);

}
