package de.sirati97.bex_proto.test.v2;

import de.sirati97.bex_proto.threading.AsyncHelper;
import de.sirati97.bex_proto.util.logging.ILogger;
import de.sirati97.bex_proto.v2.IPacket;
import de.sirati97.bex_proto.v2.artifcon.ArtifConnection;
import de.sirati97.bex_proto.v2.io.IOHandler;

/**
 * Created by sirati97 on 29.04.2016.
 */
public class TC extends ArtifConnection {
    public TC(String connectionName, AsyncHelper asyncHelper, IOHandler ioHandler, ILogger logger, IPacket packet) {
        super(connectionName, asyncHelper, ioHandler, logger, packet);
    }

    String getTC() {
        return "TC";
    }
}
