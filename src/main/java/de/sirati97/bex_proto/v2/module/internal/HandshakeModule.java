package de.sirati97.bex_proto.v2.module.internal;

import de.sirati97.bex_proto.datahandler.ArrayType;
import de.sirati97.bex_proto.datahandler.NullableType;
import de.sirati97.bex_proto.datahandler.Type;
import de.sirati97.bex_proto.util.ByteBuffer;
import de.sirati97.bex_proto.util.IConnection;
import de.sirati97.bex_proto.util.IServerConnection;
import de.sirati97.bex_proto.v2.IPacket;
import de.sirati97.bex_proto.v2.Packet;
import de.sirati97.bex_proto.v2.PacketDefinition;
import de.sirati97.bex_proto.v2.PacketExecutor;
import de.sirati97.bex_proto.v2.ReceivedPacket;
import de.sirati97.bex_proto.v2.module.HandshakeRemoteException;
import de.sirati97.bex_proto.v2.module.ModularArtifConnection;

/**
 * Created by sirati97 on 13.04.2016.
 */
public class HandshakeModule extends InternalModule<HandshakeModule.HandshakeData> implements PacketExecutor {
    private static class HandshakePacketDefinition extends PacketDefinition {
        public HandshakePacketDefinition(short id, PacketExecutor executor) {
            super(id, executor, Type.Byte, new NullableType(new ArrayType(Type.Byte)));
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
        Packet packet = new Packet(packetDefinition, action, Type.String_Utf_8.createStream(extra).getBytes());
        packet.sendTo(connection);
    }

    public void sendError(Throwable t, IConnection connection) {
        Packet packet = new Packet(packetDefinition, (byte)-1, Type.JavaThrowable.createStream(t).getBytes());
        packet.sendTo(connection);
    }

    private void send(byte action, IConnection connection) {
        Packet packet = new Packet(packetDefinition, action, null);
        packet.sendTo(connection);
    }

    @Override
    public void execute(ReceivedPacket packet) {
        byte action = packet.get(0);
        ModularArtifConnection connection = (ModularArtifConnection) packet.getSender();
        ByteBuffer extra =new ByteBuffer((byte[]) packet.get(1), connection);
        if (action == 0) { //syn
            connection.internal_OnHandshakeStarted(HandshakeModule.class);
            send((byte)1, connection); //ack
        } else if (action == 1) {
            removeModuleData(packet.getSender()).callback.callback(); //using callback because other modules will run now
        } else if (action == 2) {
            String name = (String) Type.String_Utf_8.getExtractor().extract(extra);
            connection.setConnectedWith(name);
            getModuleData(packet.getSender()).callback.callback(); //internal handshakes are all done. do the rest
            send((byte)3, connection.getConnectionName(), connection);
        } else if (action == 3) {
            String name = (String) Type.String_Utf_8.getExtractor().extract(extra);
            connection.setConnectedWith(name);
            removeModuleData(packet.getSender()).callback.callback(); //using callback because other modules will run now
        } else if (action == 4) {
            getModuleData(packet.getSender()).callback.callback(); //will check if all handshake modules completed.
        } else if (action == 5) {
            packet.getSender().getLogger().info("Handshake completed");
            removeModuleData(packet.getSender()).callback.callback(); //using callback and clean up - we are done
        } else if (action == -1) {
            Throwable t = Type.JavaThrowable.getExtractor().extract(extra);
            HandshakeData data = getModuleData(packet.getSender());
            if (data != null && data.callback != null) {
                data.callback.error(new HandshakeRemoteException(t));
            } else {
                packet.getSender().getLogger().error("Remote peer has thrown error after handshake finished. weird...", t);
            }
        }
    }

    public void sendHandshakeRequest(ModularArtifConnection connection, IHandshakeCallback handshakeCallback) {
        getOrCreateModuleData(connection).callback = handshakeCallback;
        send((byte)0, connection);
    }

    public void sendHandshakeExchangeData(ModularArtifConnection connection, IHandshakeCallback handshakeCallback) {
        getOrCreateModuleData(connection).callback = handshakeCallback;
        send((byte)2, connection.getConnectionName(), connection);
    }

    public void sendHandshakeFinished(ModularArtifConnection connection, IHandshakeCallback handshakeCallback) {
        getOrCreateModuleData(connection).callback = handshakeCallback;
        send((byte)4, connection);
    }


    public void initServerSide(ModularArtifConnection connection, IHandshakeCallback handshakeCallback) {
        getOrCreateModuleData(connection).callback = handshakeCallback;
    }

    public void finishServerSide(ModularArtifConnection connection) {
        removeModuleData(connection);
        send((byte)5, connection);
        connection.getLogger().info((connection instanceof IServerConnection ?"Client":"New peer")+ " with name '" + connection.getConnectedWith() + "' completed Handshake");
    }


}
