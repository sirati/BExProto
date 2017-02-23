package de.sirati97.bex_proto.datahandler;

import de.sirati97.bex_proto.util.bytebuffer.ByteBuffer;

public class NullableEncoder<T> extends EncoderBase<T> {
	private IType<T> base;
	
	public NullableEncoder(IType<T> base) {
		this.base = base;
	}

    @Override
    public void encode(T data, ByteBuffer buffer, boolean header) {
        if (data == null) {
            Types.Boolean.getEncoder().encode(true, buffer);
        } else {
            Types.Boolean.getEncoder().encode(false, buffer);
            base.getEncoder().encodeObj(data, buffer);
        }
    }
}
