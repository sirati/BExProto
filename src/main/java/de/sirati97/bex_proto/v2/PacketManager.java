package de.sirati97.bex_proto.v2;

import de.sirati97.bex_proto.datahandler.DerivedTypeBase;
import de.sirati97.bex_proto.datahandler.MultiStream;
import de.sirati97.bex_proto.datahandler.Stream;
import de.sirati97.bex_proto.datahandler.TypeBase;
import de.sirati97.bex_proto.util.CursorByteBuffer;

/**
 * Created by sirati97 on 15.03.2016.
 */
public class PacketManager {
    public static ReceivedPacket extract(PacketDefinition definition, CursorByteBuffer buf) {
        Object[] args = new Object[definition.getArgumentLength()];
        int counter=0;
        for (TypeBase type:definition.getTypes()) {
            Object tempObj = type.getExtractor().extract(buf);
            if (type.isArray() && type instanceof DerivedTypeBase) {
                tempObj = ((DerivedTypeBase) type).toPrimitiveArray(tempObj);
            }
            args[counter++] = tempObj;
        }
        return new ReceivedPacket(definition, buf.getIConnection(), args);
    }

    public static Stream createStream(Packet packet) {
        Stream[] streams = new Stream[packet.getArgumentLength()];
        for (int i=0;i<packet.getArgumentLength();i++) {
            streams[i] = packet.getType(i).createStream(packet.get(i));
        }
        return new MultiStream(streams);
    }
}
