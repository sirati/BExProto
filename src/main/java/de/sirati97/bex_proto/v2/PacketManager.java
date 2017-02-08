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

        for (int i=0;i<definition.getArgumentLength();) {
            IType type = definition.getTypes()[i];
            Object tempObj = type.getDecoder().decode(buf);//, i+1==definition.getArgumentLength());
            if (type.isArray() && type instanceof IDerivedType) {
                tempObj = ((IDerivedType) type).toPrimitiveArray(tempObj);
            }
            args[i] = tempObj;
        }
        return new ReceivedPacket(definition, buf.getIConnection(), args);
    }

    public static ByteBuffer createStream(Packet packet) {
        ByteBuffer buffer = new ByteBuffer();
        for (int i=0;i<packet.getArgumentLength();i++) {
            packet.getType(i).getEncoder().encodeObj(packet.get(i), buffer);//, i+1==packet.getArgumentLength());
        }
        return buffer;
    }
}
