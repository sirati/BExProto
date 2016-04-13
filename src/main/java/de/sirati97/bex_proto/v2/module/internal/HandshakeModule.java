package de.sirati97.bex_proto.v2.module.internal;

import de.sirati97.bex_proto.datahandler.NullableType;
import de.sirati97.bex_proto.datahandler.Type;
import de.sirati97.bex_proto.util.IConnection;
import de.sirati97.bex_proto.util.IServerConnection;
import de.sirati97.bex_proto.v2.IPacket;
import de.sirati97.bex_proto.v2.Packet;
import de.sirati97.bex_proto.v2.PacketDefinition;
import de.sirati97.bex_proto.v2.PacketExecutor;
import de.sirati97.bex_proto.v2.ReceivedPacket;
import de.sirati97.bex_proto.v2.module.ModularArtifConnection;

/**
 * Created by sirati97 on 13.04.2016.
 */
public class HandshakeModule extends InternalModule<HandshakeModule.HandshakeData> implements PacketExecutor {
    private static class HandshakePacketDefinition extends PacketDefinition {
        public HandshakePacketDefinition(short id, PacketExecutor executor) {
            super(id, executor, Type.Byte, new NullableType(Type.String_Utf_8));
        }
    }
    static class HandshakeData {
        public IHandshakeCallback callback;
    }
    private HandshakePacketDefinition packetDefinition;

    public HandshakeModule() {
        super((short) -2);
    }

    @Override
    protected IPacket createPacket() {
        return packetDefinition==null?(packetDefinition=new HandshakePacketDefinition(getId(), this)):packetDefinition;
    }

    @Override
    public HandshakeData createData(ModularArtifConnection connection) {
        return new HandshakeData();
    }

    private void send(byte action, String extra, IConnection connection) {
        Packet packet = new Packet(packetDefinition, action, extra);
        packet.sendTo(connection);
    }

    private void send(byte action, IConnection connection) {
        send(action, null, connection);
    }

    @Override
    public void execute(ReceivedPacket packet) {
        byte action = packet.get(0);
        String extra = packet.get(1);
        ModularArtifConnection connection = (ModularArtifConnection) packet.getSender();
        if (action == 0) { //syn
            send((byte)1, connection); //ack
        } else if (action == 1) {
            getModuleData(packet.getSender()).callback.callback(); //using callback because other modules will run now
        } else if (action == 2) {
            connection.setConnectedWith(extra);
            send((byte)3, connection.getConnectionName(), connection);
        } else if (action == 3) {
            connection.setConnectedWith(extra);
            getModuleData(packet.getSender()).callback.callback(); //using callback because other modules will run now
        } else if (action == 4) {
            packet.getSender().getLogger().info((connection instanceof IServerConnection?"Client":"New peer")+ " with name '" + connection.getConnectedWith() + "' completed Handshake");
            send((byte)5, connection);
        } else if (action == 5) {
            packet.getSender().getLogger().info("Handshake completed");
            removeModuleData(packet.getSender()).callback.callback(); //using callback and clean up - we are done
        }
    }

    public void sendHandshakeRequest(ModularArtifConnection connection, IHandshakeCallback handshakeCallback) {
        getOrCreateModuleData(connection).callback = handshakeCallback;
        send((byte)0, null, connection);
    }

    public void sendHandshakeExchangeData(ModularArtifConnection connection) {
        send((byte)2, connection.getConnectionName(), connection);
    }

    public void sendHandshakeFinished(ModularArtifConnection connection) {
        send((byte)4, null, connection);
    }

}
