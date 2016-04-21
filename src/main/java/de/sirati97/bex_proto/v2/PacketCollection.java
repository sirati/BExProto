package de.sirati97.bex_proto.v2;

import de.sirati97.bex_proto.datahandler.MultiStream;
import de.sirati97.bex_proto.datahandler.ShortStream;
import de.sirati97.bex_proto.datahandler.Stream;
import de.sirati97.bex_proto.datahandler.Type;
import de.sirati97.bex_proto.util.CursorByteBuffer;
import de.sirati97.bex_proto.util.IConnection;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sirati97 on 15.03.2016.
 */
public class PacketCollection implements IPacketCollection {
    private Map<Short, IPacket> packets = new HashMap<>();
    private short id;
    private IPacket parent;
    private PacketExecutor standardExecutor;
    private Boolean requiresReliableConnection = null;

    public PacketCollection(PacketExecutor standardExecutor) {
        this((short) 0, standardExecutor);
    }

    public PacketCollection() {
        this(null);
    }

    public PacketCollection(short id) {
        this(id, null);
    }

    public PacketCollection(short id, PacketExecutor standardExecutor) {
        this.id = id;
        this.standardExecutor = standardExecutor;
    }

    @Override
    public void extract(CursorByteBuffer buf) {
        short packetId = getShort(buf);
        if (!isAllowed(packetId, buf))return;
        IPacket packet = getPacket(packetId);
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
    public void register(IPacket packet) {
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
    public Stream createSteam(Stream streamChild, IPacket child, IConnection... iConnections) {
        Stream result = new MultiStream(new ShortStream(child.getId()), streamChild);
        return parent==null?result:parent.createSteam(result, this, iConnections);
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
    public PacketExecutor getStandardExecutor() {
        return standardExecutor;
    }

    @Override
    public IPacket getPacket(short packetId) {
        return packets.get(packetId);
    }

    @Override
    public IPacket getPacket(CursorByteBuffer buf) {
        return getPacket(getShort(buf));
    }

    private short getShort(CursorByteBuffer buf) {
        return (Short) Type.Short.getExtractor().extract(buf);
    }

}
