package de.sirati97.bex_proto.v2.module;

import de.sirati97.bex_proto.v2.IConnectionFactory;
import de.sirati97.bex_proto.v2.io.IOHandler;
import de.sirati97.bex_proto.v2.module.internal.ServerModularArtifConnection;

/**
 * Created by sirati97 on 16.04.2016.
 */
public class ModularArtifConnectionFactory implements IConnectionFactory<ModularArtifConnection> {
    private final ModuleHandler moduleHandler;

    public ModularArtifConnectionFactory(ModuleHandler moduleHandler) {
        this.moduleHandler = moduleHandler;
    }

    @Override
    public ModularArtifConnection createClient(String connectionName, IOHandler ioHandler) {
        return new ModularArtifConnection(connectionName, ioHandler, moduleHandler);
    }

    @Override
    public ModularArtifConnection createServer(IOHandler ioHandler) {
        return new ServerModularArtifConnection(ioHandler, moduleHandler);
    }
}
