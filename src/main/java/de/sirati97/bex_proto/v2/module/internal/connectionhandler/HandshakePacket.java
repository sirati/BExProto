package de.sirati97.bex_proto.v2.module.internal.connectionhandler;

import de.sirati97.bex_proto.datahandler.ArrayType;
import de.sirati97.bex_proto.datahandler.NullableType;
import de.sirati97.bex_proto.datahandler.Type;
import de.sirati97.bex_proto.util.ByteBuffer;
import de.sirati97.bex_proto.util.IConnection;
import de.sirati97.bex_proto.v2.IPacketCollection;
import de.sirati97.bex_proto.v2.Packet;
import de.sirati97.bex_proto.v2.ReceivedPacket;
import de.sirati97.bex_proto.v2.SelfExecutingPacketDefinition;
import de.sirati97.bex_proto.v2.module.HandshakeRemoteException;
import de.sirati97.bex_proto.v2.module.ModularArtifConnection;
import de.sirati97.bex_proto.v2.module.internal.connectionhandler.ConnectionHandlerModule.ConnectionHandlerData;

/**
 * Created by sirati97 on 16.04.2016.
 */
public class HandshakePacket extends SelfExecutingPacketDefinition {
    private final ConnectionHandlerModule parent;

    public HandshakePacket(IPacketCollection packetCollection, ConnectionHandlerModule parent) {
        super((short)0, packetCollection, Type.Byte, new NullableType(new ArrayType(Type.Byte)));
        this.parent = parent;
    }

    protected void send(byte action, String extra, IConnection connection) {
        Packet packet = new Packet(this, action, Type.String_Utf_8.createStream(extra).getBytes());
        packet.sendTo(connection);
    }

    public void sendError(Throwable t, IConnection connection) {
        Packet packet = new Packet(this, (byte)-1, Type.JavaThrowable.createStream(t).getBytes());
        packet.sendTo(connection);
    }

    protected void send(byte action, IConnection connection) {
        Packet packet = new Packet(this, action, null);
        packet.sendTo(connection);
    }

    @Override
    public void execute(ReceivedPacket packet) {
        byte action = packet.get(0);
        ModularArtifConnection connection = (ModularArtifConnection) packet.getSender();
        ByteBuffer extra =new ByteBuffer((byte[]) packet.get(1), connection);
        if (action == 0) { //syn
            connection.internal_OnHandshakeStarted(ConnectionHandlerModule.class);
            send((byte)1, connection); //ack
        } else if (action == 1) {
            ConnectionHandlerData data = parent.getModuleData(packet.getSender());
            if (data.handshakeState!=HandshakeState.State1) {
                errorWrongOrder(data);
            }
            data.callback.callback(); //using callback because other modules will run now
        } else if (action == 2) {
            String name = (String) Type.String_Utf_8.getExtractor().extract(extra);
            connection.setConnectedWith(name);
            parent.getModuleData(packet.getSender()).callback.callback(); //internal handshakes are all done. do the rest
            send((byte)3, connection.getConnectionName(), connection);
        } else if (action == 3) {
            ConnectionHandlerData data = parent.getModuleData(packet.getSender());
            if (data.handshakeState!=HandshakeState.State2) {
                errorWrongOrder(data);
            }
            String name = (String) Type.String_Utf_8.getExtractor().extract(extra);
            connection.setConnectedWith(name);
            data.callback.callback(); //using callback because other modules will run now
        } else if (action == 4) {
            parent.getModuleData(packet.getSender()).callback.callback(); //will check if all handshake modules completed.
        } else if (action == 5) {
            ConnectionHandlerData data = parent.getModuleData(packet.getSender());
            if (data.handshakeState!=HandshakeState.State3) {
                errorWrongOrder(data);
            }
            packet.getSender().getLogger().info("Handshake completed");
            data.handshakeState = HandshakeState.Done;
            data.callback.callback(); //using callback and clean up - we are done
            data.callback = null;
        } else if (action == -1) {
            Throwable t = Type.JavaThrowable.getExtractor().extract(extra);
            ConnectionHandlerData data = parent.getModuleData(packet.getSender());
            if (data != null && data.callback != null && data.handshakeState.isInHandshake()) {
                data.callback.error(new HandshakeRemoteException(t));
            } else {
                packet.getSender().getLogger().error("Remote peer has thrown error after handshake finished. weird...", t);
            }
        }
    }

    private void errorWrongOrder(ConnectionHandlerData data) {
        data.callback.error(new IllegalAccessException("Handshake done in wrong order. Probably malicious server"));
    }
}