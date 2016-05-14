package de.sirati97.bex_proto.v2;

import de.sirati97.bex_proto.datahandler.Stream;
import de.sirati97.bex_proto.datahandler.TypeBase;
import de.sirati97.bex_proto.util.CursorByteBuffer;
import de.sirati97.bex_proto.util.IConnection;

/**
 * Created by sirati97 on 15.03.2016.
 */
public class PacketDefinition implements IPacketDefinition, Cloneable{
    private TypeBase[] types;
    private short id;
    private PacketHandler executor;
    private IPacketDefinition parent;
    private Boolean requiresReliableConnection = null;


    PacketDefinition(short id, TypeBase... types) {
        this(id, null, null, true, null, types);
    }

    public PacketDefinition(short id, PacketHandler executor, TypeBase... types) {
        this(id, null, executor, false, null, types);
    }

    public PacketDefinition(short id, IPacketCollection parent, TypeBase... types) {
        this(id, parent, parent.getStandardExecutor(),  false, null, types);
    }

    PacketDefinition(short id, IPacketCollection parent, boolean selfExecuting, Boolean requiresReliableConnection, TypeBase... types) {
        this(id, parent, parent.getStandardExecutor(), selfExecuting, requiresReliableConnection, types);
    }

    public PacketDefinition(short id, IPacketCollection parent, PacketHandler executor, TypeBase... types) {
        this(id, parent, executor, false, null, types);
    }


    public PacketDefinition(short id, IPacketCollection parent, PacketHandler executor, Boolean requiresReliableConnection, TypeBase... types) {
        this(id, parent, executor, false, requiresReliableConnection, types);
    }

    private PacketDefinition(short id, IPacketCollection parent, PacketHandler executor, boolean selfExecuting, Boolean requiresReliableConnection, TypeBase... types) {
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
    public Stream createSteam(Stream streamChild, IPacketDefinition child, IConnection... iConnections) {
        return parent==null?streamChild:parent.createSteam(streamChild, this, iConnections);
    }

    public TypeBase[] getTypes() {
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
