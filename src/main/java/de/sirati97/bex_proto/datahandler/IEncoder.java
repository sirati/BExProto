package de.sirati97.bex_proto.datahandler;

import de.sirati97.bex_proto.util.bytebuffer.ByteBuffer;

public interface IEncoder<Type> {
    ByteBuffer encodeIndependent(Type data);
    void encode(Type data, ByteBuffer buffer);
    void encodeObj(Object data, ByteBuffer buffer);
	
}
