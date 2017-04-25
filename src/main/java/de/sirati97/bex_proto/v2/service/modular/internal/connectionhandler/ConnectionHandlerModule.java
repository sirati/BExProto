package de.sirati97.bex_proto.v2.service.modular.internal.connectionhandler;

import de.sirati97.bex_proto.datahandler.BExStatic;
import de.sirati97.bex_proto.util.IConnection;
import de.sirati97.bex_proto.util.IServerConnection;
import de.sirati97.bex_proto.v2.IPacketDefinition;
import de.sirati97.bex_proto.v2.PacketCollection;
import de.sirati97.bex_proto.v2.service.modular.ModularService;
import de.sirati97.bex_proto.v2.service.modular.internal.ICallback;
import de.sirati97.bex_proto.v2.service.modular.internal.InternalModule;

/**
 * Created by sirati97 on 13.04.2016.
 */
public class ConnectionHandlerModule extends InternalModule<ConnectionHandlerModule.ConnectionHandlerData>{
    public static class ConnectionHandlerData {
        public ICallback callback;
        public HandshakeState handshakeState = HandshakeState.NoHandshake;
        public int remoteVersion;
        public ConnectionType connectionType = ConnectionType.Server;
    }
    private PacketCollection packetCollection;
    private HandshakePacket handshakePacket;
    private YieldPacket yieldPacket;

    public ConnectionHandlerModule() {
        super((short) -2);
    }

    @Override
    protected IPacketDefinition createPacket() {
        if (packetCollection==null) {
            packetCollection = new PacketCollection(getId());
            handshakePacket = new HandshakePacket(packetCollection, this);
            yieldPacket = new YieldPacket(packetCollection, this);
        }
        return packetCollection;
    }

    @Override
    public ConnectionHandlerData createData(ModularService connection) {
        return new ConnectionHandlerData();
    }

    public void sendHandshakeRequest(ModularService connection, ICallback handshakeCallback) {
        ConnectionHandlerData data = getOrCreateModuleData(connection);
        data.callback = handshakeCallback;
        data.handshakeState = HandshakeState.State1;
        data.connectionType = ConnectionType.Client;
        handshakePacket.send((byte)0, BExStatic.VERSION_INT_CURRENT, connection);
    }

    public void sendHandshakeExchangeData(ModularService connection) {
        ConnectionHandlerData data = getModuleData(connection);
        data.handshakeState = HandshakeState.State2;
        handshakePacket.send((byte)2, connection.getConnectionName(), connection);
    }

    public void sendHandshakeFinished(ModularService connection) {
        ConnectionHandlerData data = getModuleData(connection);
        data.handshakeState = HandshakeState.State3;
        handshakePacket.send((byte)4, connection);
    }


    public void sendHandshakeError(Throwable t, IConnection connection) {
        handshakePacket.sendError(t, connection);
    }


    public void initServerSide(ModularService connection, ICallback handshakeCallback) {
        getModuleData(connection).callback = handshakeCallback;
    }

    public void finishServerSide(ModularService connection) {
        ConnectionHandlerData data = getModuleData(connection);
        data.callback = null;
        data.handshakeState = HandshakeState.Done;
        handshakePacket.send((byte)5, connection);
        connection.getLogger().info((connection instanceof IServerConnection ?"Client":"New peer")+ " with name '" + connection.getConnectedWith() + "' completed Handshake");
    }

    public void yieldRemote(IConnection connection, boolean longYield) {
        yieldPacket.yieldRemote(connection, longYield);
    }

    @Override
    protected ConnectionHandlerData removeModuleData(IConnection connection) {
        return super.removeModuleData(connection);
    }

    @Override
    protected ConnectionHandlerData getModuleData(IConnection connection) {
        return super.getModuleData(connection);
    }

    @Override
    protected ConnectionHandlerData getOrCreateModuleData(IConnection connection) {
        return super.getOrCreateModuleData(connection);
    }
}
