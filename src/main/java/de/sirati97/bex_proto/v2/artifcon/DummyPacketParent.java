package de.sirati97.bex_proto.v2.artifcon;

import de.sirati97.bex_proto.datahandler.Stream;
import de.sirati97.bex_proto.util.ByteBuffer;
import de.sirati97.bex_proto.util.IConnection;
import de.sirati97.bex_proto.util.exception.NotImplementedException;
import de.sirati97.bex_proto.v2.IPacket;

/**
 * Created by sirati97 on 15.03.2016.
 */
public class DummyPacketParent implements IPacket {
    private IPacket child;

    private DummyPacketParent(IPacket child) {
        this.child = child;
        child.setParent(this);
    }


    @Override
    public short getId() {
        throw new NotImplementedException("");
    }

    @Override
    public void setId(short id) {
        throw new NotImplementedException("");
    }

    @Override
    public Stream createSteam(Stream streamChild, IPacket child, IConnection... iConnections) {
        return streamChild;
    }

    @Override
    public void setParent(IPacket parent) {
        throw new NotImplementedException("");

    }

    @Override
    public IPacket getParent() {
        throw new NotImplementedException("");
    }

    @Override
    public void extract(ByteBuffer buf) {
        child.extract(buf);
    }
}