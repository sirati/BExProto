package de.sirati97.bex_proto.datahandler;


import de.sirati97.bex_proto.util.bytebuffer.ByteBuffer;

public class ArrayNullableEncoder<T> extends EncoderBase<T[]> {
	private INullableType<T> baseType;

	public ArrayNullableEncoder(INullableType<T> baseType) {
		this.baseType = baseType;
	}

    @Override
    public void encode(T[] data, ByteBuffer buffer) {
        boolean[] nulls = new boolean[data.length];
        for (int i=0;i<data.length;i++) {
            nulls[i] = data[i]==null;
        }

        Type.Boolean.asArray().getEncoder().encode(Type.Boolean.toObjectArray(nulls), buffer); //contains length
        for (int i=0;i<data.length;i++) {
            if (!nulls[i]) {
                baseType.getInnerType().getEncoder().encode(data[i], buffer);
            }
        }
    }
}
