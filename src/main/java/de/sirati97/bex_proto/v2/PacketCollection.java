package de.sirati97.bex_proto.v2;

import de.sirati97.bex_proto.datahandler.BExStatic;
import de.sirati97.bex_proto.datahandler.IEncoder;
import de.sirati97.bex_proto.datahandler.ShortEncoder;
import de.sirati97.bex_proto.datahandler.Type;
import de.sirati97.bex_proto.util.CursorByteBuffer;
import de.sirati97.bex_proto.util.IConnection;
import de.sirati97.bex_proto.util.bytebuffer.ByteBuffer;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sirati97 on 15.03.2016.
 */
public class PacketCollection implements IPacketCollection {
    private Map<Short, IPacketDefinition> packets = new HashMap<>();
    private short id;
    private IPacketDefinition parent;
    private PacketHandler standardExecutor;
    private Boolean requiresReliableConnection = null;

    public PacketCollection(PacketHandler standardExecutor) {
        this((short) 0, standardExecutor);
    }

    public PacketCollection() {
        this(null);
    }

    public PacketCollection(short id) {
        this(id, null);
    }

    public PacketCollection(short id, PacketHandler standardExecutor) {
        this.id = id;
        this.standardExecutor = standardExecutor;
    }

    @Override
    public void extract(CursorByteBuffer buf) {
        short packetId = getShort(buf);
        if (!isAllowed(packetId, buf))return;
        IPacketDefinition packet = getPacket(packetId);
        if (packet==null) {
            throw new IllegalStateException("There is no command handler registered for id " + packetId + " in " + getClass().toString());
        }
        packet.extract(buf);
    }

    public boolean getRequiresReliableConnection() {
        return requiresReliableConnection ==null?parent==null||parent.getRequiresReliableConnection(): requiresReliableConnection;
    }

    public void setRequiresReliableConnection(Boolean requiresReliableConnection) {
        this.requiresReliableConnection = requiresReliableConnection;
    }

    protected boolean isAllowed(short packetId, CursorByteBuffer buf){return true;}

    @Override
    public void register(IPacketDefinition packet) {
        if (packets.get(packet.getId())!=null) {
            throw new IllegalStateException("There is already a packet with the id " + packet.getId());
        }
        packets.put(packet.getId(), packet);
        packet.setParent(this);
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
    public ByteBuffer createSteam(ByteBuffer buffer, IPacketDefinition child, IConnection... iConnections) {
        BExStatic.insertShort(child.getId(), buffer);
        return parent==null?buffer:parent.createSteam(buffer, this, iConnections);
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
    public PacketHandler getStandardExecutor() {
        return standardExecutor;
    }

    @Override
    public IPacketDefinition getPacket(short packetId) {
        return packets.get(packetId);
    }

    @Override
    public IPacketDefinition getPacket(CursorByteBuffer buf) {
        return getPacket(getShort(buf));
    }

    private short getShort(CursorByteBuffer buf) {
        return (Short) Type.Short.getDecoder().decode(buf);
    }

}
