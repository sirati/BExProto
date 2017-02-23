package de.sirati97.bex_proto.v2.service.basic;

import de.sirati97.bex_proto.threading.IAsyncHelper;
import de.sirati97.bex_proto.util.logging.ILogger;
import de.sirati97.bex_proto.v2.IServiceFactory;
import de.sirati97.bex_proto.v2.IPacketDefinition;
import de.sirati97.bex_proto.v2.io.IOHandler;


/**
 * Created by sirati97 on 16.04.2016.
 */
public class ArtifServiceFactory implements IServiceFactory<BasicService> {
    private final IAsyncHelper asyncHelper;
    private final ILogger logger;
    private final IPacketDefinition packet;

    public ArtifServiceFactory(IAsyncHelper asyncHelper, ILogger logger, IPacketDefinition packet) {
        this.asyncHelper = asyncHelper;
        this.logger = logger;
        this.packet = packet;
    }

    @Override
    public BasicService createClientService(String connectionName, IOHandler ioHandler) {
        return new BasicService(connectionName, asyncHelper, ioHandler, logger, packet);
    }

    @Override
    public BasicService createServerService(IOHandler ioHandler) {
        return new BasicService("UnnamedServerConnection", asyncHelper, ioHandler, logger, packet);
    }

    @Override
    public IAsyncHelper getAsyncHelper() {
        return asyncHelper;
    }

    @Override
    public ILogger getLogger() {
        return logger;
    }

    @Override
    public Class<BasicService> getConnectionClass() {
        return BasicService.class;
    }
}
