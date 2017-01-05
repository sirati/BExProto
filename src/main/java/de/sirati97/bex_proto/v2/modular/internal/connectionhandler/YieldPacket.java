package de.sirati97.bex_proto.v2.modular.internal.connectionhandler;

import de.sirati97.bex_proto.datahandler.Type;
import de.sirati97.bex_proto.util.IConnection;
import de.sirati97.bex_proto.v2.IPacketCollection;
import de.sirati97.bex_proto.v2.Packet;
import de.sirati97.bex_proto.v2.ReceivedPacket;
import de.sirati97.bex_proto.v2.SelfHandlingPacketDefinition;
import de.sirati97.bex_proto.v2.modular.internal.YieldCause;
import de.sirati97.bex_proto.v2.modular.internal.connectionhandler.ConnectionHandlerModule.ConnectionHandlerData;

import java.util.Random;

/**
 * Created by sirati97 on 16.04.2016.
 */
public class YieldPacket extends SelfHandlingPacketDefinition {
    private final ConnectionHandlerModule parent;
    private final Random rnd = new Random();

    public YieldPacket(IPacketCollection packetCollection, ConnectionHandlerModule parent) {
        super((short)1, packetCollection, Type.Long);
        this.parent = parent;
    }

    protected void yieldRemote(IConnection connection) {
        Packet packet = new Packet(this, rnd.nextLong()); //this is done to prevent guessing encrypted packets
        packet.sendTo(connection);
    }

    @Override
    public void execute(ReceivedPacket packet) {
        ConnectionHandlerData data = parent.getModuleData(packet.getSender());
        if (data != null && data.callback != null) {
            data.callback.yield(YieldCause.PacketReceived);
        }
    }
}