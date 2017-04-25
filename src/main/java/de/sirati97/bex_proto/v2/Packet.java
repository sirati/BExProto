package de.sirati97.bex_proto.v2;

import de.sirati97.bex_proto.datahandler.IType;
import de.sirati97.bex_proto.util.IConnection;
import de.sirati97.bex_proto.util.bytebuffer.ByteBuffer;
import org.apache.commons.lang3.Validate;

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
        Validate.isTrue(data.length == getArgumentLength(), "Packet expects %s parameters", getArgumentLength());
    }

    public void set(int i, Object v) {
        Validate.isTrue(definition.getTypes()[i].isEncodable(v, false), "Packet expects parameter encodable by %s at position %d", definition.getTypes()[i].getTypeName(), i);
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
        sendTo(createStream(connections), connections);
    }

    private void sendTo(ByteBuffer stream, IConnection... connections) {
        for (IConnection connection:connections) {
            connection.send(stream, definition.getRequiresReliableConnection());
        }
    }

}
