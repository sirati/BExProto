package de.sirati97.bex_proto.v2.modular;

import de.sirati97.bex_proto.threading.AsyncHelper;
import de.sirati97.bex_proto.util.logging.ILogger;
import de.sirati97.bex_proto.v2.IConnectionServiceFactory;
import de.sirati97.bex_proto.v2.IPacketDefinition;
import de.sirati97.bex_proto.v2.io.IOHandler;
import de.sirati97.bex_proto.v2.modular.internal.ServerModularArtifConnectionService;

/**
 * Created by sirati97 on 16.04.2016.
 */
public class ModularArtifConnectionServiceFactory implements IConnectionServiceFactory<ModularArtifConnectionService> {
    private final ModuleHandler moduleHandler;

    public ModularArtifConnectionServiceFactory(ModuleHandler moduleHandler) {
        this.moduleHandler = moduleHandler;
    }

    public ModularArtifConnectionServiceFactory(AsyncHelper asyncHelper, ILogger logger, IPacketDefinition packetHandler) {
        this(new ModuleHandler(asyncHelper, logger, packetHandler));
    }


    @Override
    public ModularArtifConnectionService createClient(String connectionName, IOHandler ioHandler) {
        return new ModularArtifConnectionService(connectionName, ioHandler, moduleHandler);
    }

    @Override
    public ModularArtifConnectionService createServer(IOHandler ioHandler) {
        return new ServerModularArtifConnectionService(ioHandler, moduleHandler);
    }

    @Override
    public AsyncHelper getAsyncHelper() {
        return moduleHandler.getAsyncHelper();
    }

    @Override
    public ILogger getLogger() {
        return moduleHandler.getLogger();
    }

    @Override
    public Class<ModularArtifConnectionService> getConnectionClass() {
        return ModularArtifConnectionService.class;
    }
}
