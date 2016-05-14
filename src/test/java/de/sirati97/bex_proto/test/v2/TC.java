package de.sirati97.bex_proto.test.v2;

import de.sirati97.bex_proto.threading.AsyncHelper;
import de.sirati97.bex_proto.util.logging.ILogger;
import de.sirati97.bex_proto.v2.IPacketDefinition;
import de.sirati97.bex_proto.v2.artifcon.ArtifConnection;
import de.sirati97.bex_proto.v2.io.IOHandler;

/**
 * Created by sirati97 on 29.04.2016.
 */
public class TC extends ArtifConnection {
    public TC(String connectionName, AsyncHelper asyncHelper, IOHandler ioHandler, ILogger logger, IPacketDefinition packet) {
        super(connectionName, asyncHelper, ioHandler, logger, packet);
    }

    String getTC() {
        return "TC";
    }
}
