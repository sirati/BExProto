package de.sirati97.bex_proto.datahandler;

import de.sirati97.bex_proto.util.CursorByteBuffer;
import de.sirati97.bex_proto.util.bytebuffer.ByteBuffer;

/**
 * Created by sirati97 on 07.07.2016 for BexProto.
 */
public class NullReferenceType extends ObjType<Object> {
    private final StreamExtractor extractor = new StreamExtractor() {
        @Override
        public Object extract(CursorByteBuffer dat) {
            return null;
        }
    };
    private final Stream stream = new Stream() {
        @Override
        public ByteBuffer getByteBuffer() {
            return new ByteBuffer();
        }
    };

    @Override
    public boolean isEncodable(Object obj, boolean platformIndependent) {
        return obj == null;
    }

    @Override
    public StreamExtractor<Object> getExtractor() {
        return null;
    }

    @Override
    public Class<Object> getType() {
        return Object.class;
    }

    @Override
    public String getTypeName() {
        return "nil";
    }

    @Override
    public Stream createStreamCasted(Object obj) {
        if (obj != null) {
            throw new IllegalStateException("This type is used for serializing null references. It should not be used anywhere else");
        }
        return stream;
    }
}
