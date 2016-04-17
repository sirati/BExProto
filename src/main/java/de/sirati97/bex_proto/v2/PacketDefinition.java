package de.sirati97.bex_proto.v2;

import de.sirati97.bex_proto.datahandler.Stream;
import de.sirati97.bex_proto.datahandler.TypeBase;
import de.sirati97.bex_proto.util.ByteBuffer;
import de.sirati97.bex_proto.util.IConnection;

/**
 * Created by sirati97 on 15.03.2016.
 */
public class PacketDefinition implements IPacket, Cloneable{
    private TypeBase[] types;
    private short id;
    private PacketExecutor executor;
    private IPacket parent;


    PacketDefinition(short id, TypeBase... types) {
        this(id, null, null, true, types);
    }

    public PacketDefinition(short id, PacketExecutor executor, TypeBase... types) {
        this(id, null, executor, types);
    }

    public PacketDefinition(short id, IPacketCollection parent, TypeBase... types) {
        this(id, parent,  false, types);
    }

    PacketDefinition(short id, IPacketCollection parent, boolean selfExecuting, TypeBase... types) {
        this(id, parent, parent.getStandardExecutor(), selfExecuting, types);
    }

    public PacketDefinition(short id, IPacketCollection parent, PacketExecutor executor, TypeBase... types) {
        this(id, parent, executor, false, types);
    }

    private PacketDefinition(short id, IPacketCollection parent, PacketExecutor executor, boolean selfExecuting, TypeBase... types) {
        this.id = id;
        if (parent != null) {
            parent.register(this);
        }
        this.executor = selfExecuting? (PacketExecutor) this :executor;
        this.types = types;
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
    public Stream createSteam(Stream streamChild, IPacket child, IConnection... iConnections) {
        return parent==null?streamChild:parent.createSteam(streamChild, this, iConnections);
    }

    public TypeBase[] getTypes() {
        return types;
    }

    public int getArgumentLength() {
        return types.length;
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
    public void extract(ByteBuffer buf) {
        ReceivedPacket packet = PacketManager.extract(this, buf);
        executor.execute(packet);
    }

    public PacketExecutor getExecutor() {
        return executor;
    }

    @Override
    public PacketDefinition clone() {
        return clone(parent);
    }


    public PacketDefinition clone(IPacket newParent) {
        PacketDefinition result = new PacketDefinition(id, null, executor, executor==this, types);
        result.setParent(newParent);
        return result;
    }
}
