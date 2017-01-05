package de.sirati97.bex_proto.v2.modular;

import de.sirati97.bex_proto.datahandler.Stream;
import de.sirati97.bex_proto.threading.AsyncHelper;
import de.sirati97.bex_proto.util.CursorByteBuffer;
import de.sirati97.bex_proto.util.IConnection;
import de.sirati97.bex_proto.util.logging.ILogger;
import de.sirati97.bex_proto.v2.IPacketDefinition;
import de.sirati97.bex_proto.v2.IdPacketWrapper;
import de.sirati97.bex_proto.v2.PacketCollection;
import de.sirati97.bex_proto.v2.modular.internal.InternalModule;
import de.sirati97.bex_proto.v2.modular.internal.connectionhandler.ConnectionHandlerModule;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sirati97 on 12.04.2016.
 */
public class ModuleHandler {
    private boolean allowRegistration = false;
    private final PacketCollection packets = new PacketCollection() {
        @Override
        public void register(IPacketDefinition packet) {
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

    public ModuleHandler(AsyncHelper asyncHelper, ILogger logger, IPacketDefinition packetHandler) {
        this.asyncHelper = asyncHelper;
        this.logger = logger;
        register(new PacketDefinitionWrapper(packetHandler)); //-1
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

    private void register(short id, IPacketDefinition packet) {
        register(new IdPacketWrapper(id, packet));
    }

    private void register(short id, boolean encapsulate, IPacketDefinition packet) {
        register(encapsulate?new IdPacketWrapper(id, packet):packet);
    }
    private void register(IPacketDefinition packet) {
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
        if (!(connection instanceof ModularArtifConnectionService)) {
            throw new IllegalStateException("Connection does not support modules");
        }
    }
    private class PacketDefinitionWrapper extends IdPacketWrapper {
        public PacketDefinitionWrapper(IPacketDefinition child) {
            super((short) -1, child);
        }

        @Override
        public void extract(CursorByteBuffer buf) {
            if (buf.getIConnection() instanceof ModularArtifConnectionService && !((ModularArtifConnectionService) buf.getIConnection()).isConnectionEstablished()) {
                error();
            }
            super.extract(buf);
        }

        @Override
        public Stream createSteam(Stream streamChild, IPacketDefinition child, IConnection... iConnections) {
            for (IConnection connection:iConnections) {
                if (connection instanceof ModularArtifConnectionService && !((ModularArtifConnectionService) connection).isConnectionEstablished()) {
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
