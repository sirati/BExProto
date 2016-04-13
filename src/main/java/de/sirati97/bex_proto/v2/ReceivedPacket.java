package de.sirati97.bex_proto.v2;

import de.sirati97.bex_proto.util.IConnection;

/**
 * Created by sirati97 on 15.03.2016.
 */
public class ReceivedPacket extends Packet {
    private IConnection sender;

    public ReceivedPacket(PacketDefinition definition, IConnection sender, Object... data) {
        super(definition, data);
        this.sender = sender;
    }

    public ReceivedPacket(PacketDefinition definition, IConnection sender) {
        super(definition);
        this.sender = sender;
    }

    public IConnection getSender() {
        return sender;
    }
}
