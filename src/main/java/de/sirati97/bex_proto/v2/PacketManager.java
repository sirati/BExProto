package de.sirati97.bex_proto.v2;

import de.sirati97.bex_proto.datahandler.IDerivedType;
import de.sirati97.bex_proto.datahandler.IType;
import de.sirati97.bex_proto.util.CursorByteBuffer;
import de.sirati97.bex_proto.util.bytebuffer.ByteBuffer;

/**
 * Created by sirati97 on 15.03.2016.
 */
public class PacketManager {
    public static ReceivedPacket extract(PacketDefinition definition, CursorByteBuffer buf) {
        Object[] args = new Object[definition.getArgumentLength()];
        int counter=0;
        for (IType type:definition.getTypes()) {
            Object tempObj = type.getDecoder().decode(buf);
            if (type.isArray() && type instanceof IDerivedType) {
                tempObj = ((IDerivedType) type).toPrimitiveArray(tempObj);
            }
            args[counter++] = tempObj;
        }
        return new ReceivedPacket(definition, buf.getIConnection(), args);
    }

    public static ByteBuffer createStream(Packet packet) {
        ByteBuffer buffer = new ByteBuffer();
        for (int i=0;i<packet.getArgumentLength();i++) {
            packet.getType(i).getEncoder().encodeObj(packet.get(i), buffer);
        }
        return buffer;
    }
}
