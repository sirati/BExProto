package de.sirati97.bex_proto.datahandler;

import de.sirati97.bex_proto.util.bytebuffer.ByteBuffer;

public class NullableEncoder<T> extends EncoderBase<T> {
	private IType<T> base;
	
	public NullableEncoder(IType<T> base) {
		this.base = base;
	}

    @Override
    public void encode(T data, ByteBuffer buffer) {
        if (data == null) {
            Type.Boolean.getEncoder().encode(true, buffer);
        } else {
            Type.Boolean.getEncoder().encode(false, buffer);
            base.getEncoder().encodeObj(data, buffer);
        }
    }
}
