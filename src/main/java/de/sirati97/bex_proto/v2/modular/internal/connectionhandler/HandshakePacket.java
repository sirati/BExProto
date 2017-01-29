package de.sirati97.bex_proto.v2.modular.internal.connectionhandler;

import de.sirati97.bex_proto.datahandler.BExStatic;
import de.sirati97.bex_proto.datahandler.Type;
import de.sirati97.bex_proto.util.CursorByteBuffer;
import de.sirati97.bex_proto.util.IConnection;
import de.sirati97.bex_proto.v2.IPacketCollection;
import de.sirati97.bex_proto.v2.Packet;
import de.sirati97.bex_proto.v2.ReceivedPacket;
import de.sirati97.bex_proto.v2.SelfHandlingPacketDefinition;
import de.sirati97.bex_proto.v2.modular.HandshakeMismatchVersionException;
import de.sirati97.bex_proto.v2.modular.HandshakeRemoteException;
import de.sirati97.bex_proto.v2.modular.ModularArtifConnectionService;
import de.sirati97.bex_proto.v2.modular.internal.connectionhandler.ConnectionHandlerModule.ConnectionHandlerData;

/**
 * Created by sirati97 on 16.04.2016.
 */
public class HandshakePacket extends SelfHandlingPacketDefinition {
    private final ConnectionHandlerModule parent;

    public HandshakePacket(IPacketCollection packetCollection, ConnectionHandlerModule parent) {
        super((short)0, packetCollection, Type.Byte, Type.Byte.asArray().asNullable());
        this.parent = parent;
    }

    protected void send(byte action, String extra, IConnection connection) {
        Packet packet = new Packet(this, action, Type.String_Utf_8.getEncoder().encodeIndependent(extra).getBytes());
        packet.sendTo(connection);
    }

    protected void send(byte action, int extra, IConnection connection) {
        Packet packet = new Packet(this, action, Type.Integer.getEncoder().encodeIndependent(extra).getBytes());
        packet.sendTo(connection);
    }

    public void sendError(Throwable t, IConnection connection) {
        Packet packet = new Packet(this, (byte)-1, Type.JavaThrowable.getEncoder().encodeIndependent(t).getBytes());
        packet.sendTo(connection);
    }

    protected void send(byte action, IConnection connection) {
        Packet packet = new Packet(this, action, null);
        packet.sendTo(connection);
    }

    @Override
    public void execute(ReceivedPacket packet) {
        byte action = packet.get(0);
        ModularArtifConnectionService connection = (ModularArtifConnectionService) packet.getSender();
        CursorByteBuffer extra =new CursorByteBuffer((byte[]) packet.get(1), connection);
        ConnectionHandlerData data = action==0?parent.getOrCreateModuleData(connection):parent.getModuleData(connection);

        switch (action) {


            //Server side
            case 0: //syn
                data.handshakeState = HandshakeState.State1;
                data.remoteVersion = Type.Integer.getDecoder().decode(extra);
                if (data.remoteVersion < BExStatic.VERSION_INT_MIN) {
                    sendError(connection.internal_CancelMismatchVersion(ConnectionHandlerModule.class, data.remoteVersion) ,connection);
                } else {
                    connection.internal_OnHandshakeStarted(ConnectionHandlerModule.class);
                    send((byte)1, BExStatic.VERSION_INT_CURRENT, connection); //ack
                }
                break;

            case 2:
                if (checkOrder(data, HandshakeState.State1)) {
                    data.handshakeState = HandshakeState.State2;
                    String name = Type.String_Utf_8.getDecoder().decode(extra);
                    connection.setConnectedWith(name);
                    data.callback.callback(); //internal handshakes are all done. do the rest
                    send((byte)3, connection.getConnectionName(), connection);
                }
                break;

            case 4:
                if (checkOrder(data, HandshakeState.State2)) {
                    data.handshakeState = HandshakeState.State3;
                    data.callback.callback(); //will check if all handshake modules completed.
                }
                break;




            //Client side
            case 1:
                if (checkOrder(data, HandshakeState.State1)) {
                    data.remoteVersion = Type.Integer.getDecoder().decode(extra);
                    if (data.remoteVersion < BExStatic.VERSION_INT_MIN) {
                        data.callback.error(new HandshakeMismatchVersionException(data.remoteVersion));
                    } else {
                        data.callback.callback(); //using callback because other modules will run now
                    }
                }
                break;

            case 3:
                if (checkOrder(data, HandshakeState.State2)) {
                    String name = Type.String_Utf_8.getDecoder().decode(extra);
                    connection.setConnectedWith(name);
                    data.callback.callback(); //using callback because other modules will run now
                }
                break;

            case 5:
                if (checkOrder(data, HandshakeState.State3)) {
                    packet.getSender().getLogger().info("Handshake completed");
                    data.handshakeState = HandshakeState.Done;
                    data.callback.callback(); //using callback and clean up - we are done
                    data.callback = null;
                }
                break;




            //Both
            case -1:
                Throwable t = Type.JavaThrowable.getDecoder().decode(extra);
                if (data != null && data.callback != null && data.handshakeState.isInHandshake()) {
                    data.callback.error(new HandshakeRemoteException(t));
                } else {
                    packet.getSender().getLogger().error("Remote peer has thrown error after handshake finished. weird...", t);
                }
                break;
            default:
        }
    }

    private boolean checkOrder(ConnectionHandlerData data, HandshakeState state) {
        boolean result = data.handshakeState == state;
        if (!result) {
            data.callback.error(new IllegalAccessException("Handshake done in wrong order. Probably malicious " + data.connectionType.getOtherSide().toString().toLowerCase()));
        }
        return result;
    }
}