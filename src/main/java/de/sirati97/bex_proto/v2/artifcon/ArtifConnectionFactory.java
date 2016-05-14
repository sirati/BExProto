package de.sirati97.bex_proto.v2.artifcon;

import de.sirati97.bex_proto.threading.AsyncHelper;
import de.sirati97.bex_proto.util.logging.ILogger;
import de.sirati97.bex_proto.v2.IConnectionFactory;
import de.sirati97.bex_proto.v2.IPacketDefinition;
import de.sirati97.bex_proto.v2.io.IOHandler;


/**
 * Created by sirati97 on 16.04.2016.
 */
public class ArtifConnectionFactory implements IConnectionFactory<ArtifConnection> {
    private final AsyncHelper asyncHelper;
    private final ILogger logger;
    private final IPacketDefinition packet;

    public ArtifConnectionFactory(AsyncHelper asyncHelper, ILogger logger, IPacketDefinition packet) {
        this.asyncHelper = asyncHelper;
        this.logger = logger;
        this.packet = packet;
    }

    @Override
    public ArtifConnection createClient(String connectionName, IOHandler ioHandler) {
        return new ArtifConnection(connectionName, asyncHelper, ioHandler, logger, packet);
    }

    @Override
    public ArtifConnection createServer(IOHandler ioHandler) {
        return new ArtifConnection("UnnamedServerConnection", asyncHelper, ioHandler, logger, packet);
    }

    @Override
    public AsyncHelper getAsyncHelper() {
        return asyncHelper;
    }

    @Override
    public ILogger getLogger() {
        return logger;
    }

    @Override
    public Class<ArtifConnection> getConnectionClass() {
        return ArtifConnection.class;
    }
}
