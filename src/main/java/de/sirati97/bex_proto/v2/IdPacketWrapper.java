package de.sirati97.bex_proto.v2;

import de.sirati97.bex_proto.datahandler.Stream;
import de.sirati97.bex_proto.util.CursorByteBuffer;
import de.sirati97.bex_proto.util.IConnection;

/**
 * Created by sirati97 on 12.04.2016.
 */
public class IdPacketWrapper implements IPacket {
    private final short id;
    private IPacket child;
    private IPacket parent;
    private Boolean requiresReliableConnection = new Boolean(true);

    public IdPacketWrapper(short id, IPacket child) {
        this.id = id;
        setChild(child);
    }

    @Override
    public short getId() {
        return id;
    }

    @Override
    public void setId(short id) {}

    @Override
    public Stream createSteam(Stream streamChild, IPacket child, IConnection... iConnections) {
        return getParent().createSteam(streamChild, child, iConnections);
    }

    @Override
    public void setParent(IPacket parent) {
        this.parent = parent;
    }

    @Override
    public IPacket getParent() {
        return parent;
    }

    @Override
    public void extract(CursorByteBuffer buf) {
        getChild().extract(buf);
    }


    @Override
    public boolean getRequiresReliableConnection() {
        return requiresReliableConnection ==null?parent==null||parent.getRequiresReliableConnection(): requiresReliableConnection;
    }

    @Override
    public void setRequiresReliableConnection(Boolean requiresReliableConnection) {
        this.requiresReliableConnection = requiresReliableConnection;
    }

    public IPacket getChild() {
        return child;
    }

    public void setChild(IPacket child) {
        this.child = child;
        child.setId(getId());
        child.setParent(this);
    }
}
