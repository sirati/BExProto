package de.sirati97.bex_proto.v2.service.modular;

import de.sirati97.bex_proto.threading.AsyncHelper;
import de.sirati97.bex_proto.util.logging.ILogger;
import de.sirati97.bex_proto.v2.IServiceFactory;
import de.sirati97.bex_proto.v2.IPacketDefinition;
import de.sirati97.bex_proto.v2.io.IOHandler;
import de.sirati97.bex_proto.v2.service.modular.internal.ServerModularService;

/**
 * Created by sirati97 on 16.04.2016.
 */
public class ModularServiceFactory implements IServiceFactory<ModularService> {
    private final ModuleHandler moduleHandler;

    public ModularServiceFactory(ModuleHandler moduleHandler) {
        this.moduleHandler = moduleHandler;
    }

    public ModularServiceFactory(AsyncHelper asyncHelper, ILogger logger, IPacketDefinition packetHandler) {
        this(new ModuleHandler(asyncHelper, logger, packetHandler));
    }


    @Override
    public ModularService createClientService(String connectionName, IOHandler ioHandler) {
        return new ModularService(connectionName, ioHandler, moduleHandler);
    }

    @Override
    public ModularService createServerService(IOHandler ioHandler) {
        return new ServerModularService(ioHandler, moduleHandler);
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
    public Class<ModularService> getConnectionClass() {
        return ModularService.class;
    }
}
