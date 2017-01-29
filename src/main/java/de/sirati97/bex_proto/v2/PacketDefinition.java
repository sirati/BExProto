package de.sirati97.bex_proto.v2;

import de.sirati97.bex_proto.datahandler.IType;
import de.sirati97.bex_proto.util.CursorByteBuffer;
import de.sirati97.bex_proto.util.IConnection;
import de.sirati97.bex_proto.util.bytebuffer.ByteBuffer;

/**
 * Created by sirati97 on 15.03.2016.
 */
public class PacketDefinition implements IPacketDefinition, Cloneable{
    private IType[] types;
    private short id;
    private PacketHandler executor;
    private IPacketDefinition parent;
    private Boolean requiresReliableConnection = null;


    PacketDefinition(short id, IType... types) {
        this(id, null, null, true, null, types);
    }

    public PacketDefinition(short id, PacketHandler executor, IType... types) {
        this(id, null, executor, false, null, types);
    }

    public PacketDefinition(short id, IPacketCollection parent, IType... types) {
        this(id, parent, parent.getStandardExecutor(),  false, null, types);
    }

    PacketDefinition(short id, IPacketCollection parent, boolean selfExecuting, Boolean requiresReliableConnection, IType... types) {
        this(id, parent, parent==null?null:parent.getStandardExecutor(), selfExecuting, requiresReliableConnection, types);
    }

    public PacketDefinition(short id, IPacketCollection parent, PacketHandler executor, IType... types) {
        this(id, parent, executor, false, null, types);
    }


    public PacketDefinition(short id, IPacketCollection parent, PacketHandler executor, Boolean requiresReliableConnection, IType... types) {
        this(id, parent, executor, false, requiresReliableConnection, types);
    }

    private PacketDefinition(short id, IPacketCollection parent, PacketHandler executor, boolean selfExecuting, Boolean requiresReliableConnection, IType... types) {
        this.id = id;
        if (parent != null) {
            parent.register(this);
        }
        this.executor = selfExecuting? (PacketHandler) this :executor;
        this.types = types;
        this.requiresReliableConnection = requiresReliableConnection;
    }

    @Override
    public short getId() {
        return id;
    }

    @Override
    public void setId(short id) {
        this.id = id;
    }

    @Override
    public ByteBuffer createSteam(ByteBuffer stream, IPacketDefinition child, IConnection... iConnections) {
        return parent==null? stream :parent.createSteam(stream, this, iConnections);
    }

    public IType[] getTypes() {
        return types;
    }

    public int getArgumentLength() {
        return types.length;
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
        ReceivedPacket packet = PacketManager.extract(this, buf);
        executor.execute(packet);
    }

    public PacketHandler getExecutor() {
        return executor;
    }

    @Override
    public PacketDefinition clone() {
        return clone(parent);
    }


    public PacketDefinition clone(IPacketDefinition newParent) {
        PacketDefinition result = new PacketDefinition(id, null, executor, executor==this, requiresReliableConnection, types);
        result.setParent(newParent);
        return result;
    }

    public boolean getRequiresReliableConnection() {
        return requiresReliableConnection ==null?parent==null||parent.getRequiresReliableConnection(): requiresReliableConnection;
    }

    public void setRequiresReliableConnection(Boolean requiresReliableConnection) {
        this.requiresReliableConnection = requiresReliableConnection;
    }
}
