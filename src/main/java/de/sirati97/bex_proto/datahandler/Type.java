package de.sirati97.bex_proto.datahandler;

import java.lang.reflect.Array;

public abstract class Type<T> extends CommonTypeBase<T>{
    public Type(IEncoder<T> encoder, IDecoder<T> decoder) {
        super(encoder, decoder);
        if(earlyRegister())register();
    }
	
	protected boolean earlyRegister() {
		return true;
	}
	
	protected void register() {
        Types.register(this);
	}

    @SuppressWarnings("unchecked")
    @Override
    public T[] createArray(int length) {
        return (T[]) Array.newInstance(getObjType(), length);
    }
	
	@Override public boolean isArray() {return false;}

	static {
	    Types.init();
    }
}
