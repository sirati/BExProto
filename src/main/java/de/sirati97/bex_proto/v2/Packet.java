package de.sirati97.bex_proto.v2;

import de.sirati97.bex_proto.datahandler.IType;
import de.sirati97.bex_proto.util.IConnection;
import de.sirati97.bex_proto.util.bytebuffer.ByteBuffer;

/**
 * Created by sirati97 on 15.03.2016.
 */
public class Packet {
    private final PacketDefinition definition;
    private final Object[] data;

    public Packet(PacketDefinition definition) {
        this(definition, new Object[definition.getArgumentLength()]);
    }

    public Packet(PacketDefinition definition, Object... data) {
        this.definition = definition;
        this.data = data;
    }

    public void set(int i, Object v) {
        data[i]=v;
    }

    public <T> T get(int i) {
        return (T)data[i];
    }

    public int getArgumentLength() {
        return getDefinition().getArgumentLength();
    }

    public PacketDefinition getDefinition() {
        return definition;
    }

    public boolean checkPacket(Class<? extends PacketDefinition> clazz) {
        return clazz.isInstance(definition);
    }

    public IType getType(int i) {
        return getDefinition().getTypes()[i];
    }

    public ByteBuffer createStream(IConnection... connections) {
        return getDefinition().createSteam(PacketManager.createStream(this), null, connections);
    }

    public void sendTo(IConnection... connections) {
        ByteBuffer stream = createStream(connections);
        for (IConnection connection:connections) {
            connection.send(stream, definition.getRequiresReliableConnection());
        }
    }
}
