package de.sirati97.bex_proto.v2;

import de.sirati97.bex_proto.util.CursorByteBuffer;
import de.sirati97.bex_proto.util.IConnection;
import de.sirati97.bex_proto.util.bytebuffer.ByteBuffer;

/**
 * Created by sirati97 on 15.03.2016.
 */
public interface IPacketDefinition {
    short getId();

    void setId(short id);

    ByteBuffer createSteam(ByteBuffer buffer, IPacketDefinition child, IConnection... iConnections);

    void setParent(IPacketDefinition parent);

    IPacketDefinition getParent();

    void extract(CursorByteBuffer buf);

    boolean getRequiresReliableConnection();

    void setRequiresReliableConnection(Boolean requiringReliableConnection);
}
