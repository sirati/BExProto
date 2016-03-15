package de.sirati97.bex_proto.v2;

import de.sirati97.bex_proto.util.ByteBuffer;

/**
 * Created by sirati97 on 15.03.2016.
 */
public interface IPacketCollection extends IPacket {
    PacketExecutor getStandardExecutor();
    IPacket getPacket(short id);
    IPacket getPacket(ByteBuffer buf);
    void register(IPacket packet);
}
