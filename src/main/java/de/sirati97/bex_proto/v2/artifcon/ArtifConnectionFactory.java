package de.sirati97.bex_proto.v2.artifcon;

import de.sirati97.bex_proto.threading.AsyncHelper;
import de.sirati97.bex_proto.util.logging.ILogger;
import de.sirati97.bex_proto.v2.IConnectionFactory;
import de.sirati97.bex_proto.v2.IPacket;

/**
 * Created by sirati97 on 16.04.2016.
 */
public class ArtifConnectionFactory implements IConnectionFactory {
    private final AsyncHelper asyncHelper;
    private final ILogger logger;
    private final IPacket packet;

    public ArtifConnectionFactory(AsyncHelper asyncHelper, ILogger logger, IPacket packet) {
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
}
