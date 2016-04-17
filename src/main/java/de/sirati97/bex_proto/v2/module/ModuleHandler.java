package de.sirati97.bex_proto.v2.module;

import de.sirati97.bex_proto.datahandler.Stream;
import de.sirati97.bex_proto.threading.AsyncHelper;
import de.sirati97.bex_proto.util.ByteBuffer;
import de.sirati97.bex_proto.util.IConnection;
import de.sirati97.bex_proto.util.logging.ILogger;
import de.sirati97.bex_proto.v2.IPacket;
import de.sirati97.bex_proto.v2.IdPacketWrapper;
import de.sirati97.bex_proto.v2.PacketCollection;
import de.sirati97.bex_proto.v2.module.internal.InternalModule;
import de.sirati97.bex_proto.v2.module.internal.connectionhandler.ConnectionHandlerModule;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sirati97 on 12.04.2016.
 */
public class ModuleHandler {
    private boolean allowRegistration = false;
    private final PacketCollection packets = new PacketCollection() {
        @Override
        public void register(IPacket packet) {
            if (!allowRegistration) {
                throw new IllegalStateException(new IllegalAccessException("You are not allowed to register packets manually"));
            }
            super.register(packet);
        }
    };
    final List<IModuleHandshake> handshakesInternalPriority = new ArrayList<>(1);
    final List<IModuleHandshake> handshakesHighPriority = new ArrayList<>(1);
    final List<IModuleHandshake> handshakesLowPriority = new ArrayList<>(1);
    final ConnectionHandlerModule connectionHandlerModule = new ConnectionHandlerModule();
    private final AsyncHelper asyncHelper;
    private final ILogger logger;

    public ModuleHandler(IPacket packetHandler, AsyncHelper asyncHelper, ILogger logger) {
        this.asyncHelper = asyncHelper;
        this.logger = logger;
        register(new PacketWrapper(packetHandler)); //-1
        register(connectionHandlerModule); //-2

    }

    public void register(IModule module) {
        boolean internalModule = module instanceof InternalModule;
        if (internalModule) {
            if (module.getId() >= -1) {
                throw new IllegalStateException("Internal Error");
            }
        } else if (module.getId() < 0) {
            throw new IllegalStateException("Id has to be positive or 0");
        }
        register(module.getId(), !internalModule, module.getPacket());
        if (module instanceof IModuleHandshake) {
            IModuleHandshake moduleHandshake = (IModuleHandshake) module;
            (moduleHandshake instanceof InternalModule?handshakesInternalPriority:(moduleHandshake.hasHighHandshakePriority()?handshakesHighPriority:handshakesLowPriority)).add(moduleHandshake);
        }
    }

    private void register(short id, IPacket packet) {
        register(new IdPacketWrapper(id, packet));
    }

    private void register(short id, boolean encapsulate, IPacket packet) {
        register(encapsulate?new IdPacketWrapper(id, packet):packet);
    }
    private void register(IPacket packet) {
        allowRegistration = true;
        packets.register(packet);
        allowRegistration = false;
    }

    PacketCollection getPacketHandler() {
        return packets;
    }

    public AsyncHelper getAsyncHelper() {
        return asyncHelper;
    }

    public ILogger getLogger() {
        return logger;
    }

    static void assertConnectionModuleSupport(IConnection connection) {
        if (!(connection instanceof ModularArtifConnection)) {
            throw new IllegalStateException("Connection does not support modules");
        }
    }
    private class PacketWrapper extends IdPacketWrapper {
        public PacketWrapper(IPacket child) {
            super((short) -1, child);
        }

        @Override
        public void extract(ByteBuffer buf) {
            if (buf.getIConnection() instanceof ModularArtifConnection && !((ModularArtifConnection) buf.getIConnection()).isConnectionEstablished()) {
                error();
            }
            super.extract(buf);
        }

        @Override
        public Stream createSteam(Stream streamChild, IPacket child, IConnection... iConnections) {
            for (IConnection connection:iConnections) {
                if (connection instanceof ModularArtifConnection && !((ModularArtifConnection) connection).isConnectionEstablished()) {
                    error();
                }
            }
            return super.createSteam(streamChild, child, iConnections);
        }

        private void error() throws IllegalStateException {
            throw new IllegalStateException("Connection is not established yet. Please wait until handshake is complete!");
        }
    }

}
