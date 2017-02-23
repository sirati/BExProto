package de.sirati97.bex_proto.v2;

import de.sirati97.bex_proto.util.CursorByteBuffer;

/**
 * Created by sirati97 on 15.03.2016.
 */
public interface IPacketCollection extends IPacketDefinition {
    IPacketHandler getStandardExecutor();
    IPacketDefinition getPacket(short id);
    IPacketDefinition getPacket(CursorByteBuffer buf);
    void register(IPacketDefinition packet);
}
