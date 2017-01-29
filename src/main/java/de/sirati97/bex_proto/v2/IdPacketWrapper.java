package de.sirati97.bex_proto.v2;

import de.sirati97.bex_proto.util.CursorByteBuffer;
import de.sirati97.bex_proto.util.IConnection;
import de.sirati97.bex_proto.util.bytebuffer.ByteBuffer;

/**
 * Created by sirati97 on 12.04.2016.
 */
public class IdPacketWrapper implements IPacketDefinition {
    private final short id;
    private IPacketDefinition child;
    private IPacketDefinition parent;
    private Boolean requiresReliableConnection = new Boolean(true);

    public IdPacketWrapper(short id, IPacketDefinition child) {
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
    public ByteBuffer createSteam(ByteBuffer stream, IPacketDefinition child, IConnection... iConnections) {
        return getParent().createSteam(stream, child, iConnections);
    }

    @Override
    public void setParent(IPacketDefinition parent) {
        this.parent = parent;
    }

    @Override
    public IPacketDefinition getParent() {
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

    public IPacketDefinition getChild() {
        return child;
    }

    public void setChild(IPacketDefinition child) {
        this.child = child;
        child.setId(getId());
        child.setParent(this);
    }
}
