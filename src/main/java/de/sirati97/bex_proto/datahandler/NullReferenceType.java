package de.sirati97.bex_proto.datahandler;

import de.sirati97.bex_proto.util.CursorByteBuffer;
import de.sirati97.bex_proto.util.bytebuffer.ByteBuffer;

/**
 * Created by sirati97 on 07.07.2016 for BexProto.
 */
public class NullReferenceType extends ObjType<Object> {

    public NullReferenceType(IEncoder<Object> encoder, IDecoder<Object> decoder) {
        super(new EncoderBase<Object>() {
            @Override
            public void encode(Object data, ByteBuffer buffer, boolean header) {
                if (data != null) {
                    throw new IllegalStateException("This type is used for serializing null references. It should not be used anywhere else");
                }
            }
        }, new DecoderBase<Object>() {
            @Override
            public Object decode(CursorByteBuffer dat, boolean header) {
                return null;
            }
        });
    }

    @Override
    public boolean isEncodable(Object obj, boolean platformIndependent) {
        return obj == null;
    }

    @Override
    public Class<Object> getType() {
        return Object.class;
    }

    @Override
    public String getTypeName() {
        return "nil";
    }
}
