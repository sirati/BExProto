package de.sirati97.bex_proto.v2;

import de.sirati97.bex_proto.datahandler.Stream;
import de.sirati97.bex_proto.util.ByteBuffer;
import de.sirati97.bex_proto.util.IConnection;

/**
 * Created by sirati97 on 15.03.2016.
 */
public interface IPacket {
    short getId();

    void setId(short id);

    Stream createSteam(Stream streamChild, IPacket child, IConnection... iConnections);

    void setParent(IPacket parent);

    IPacket getParent();

    void extract(ByteBuffer buf);
}
