package de.sirati97.bex_proto.v2;

import de.sirati97.bex_proto.threading.AsyncHelper;
import de.sirati97.bex_proto.util.logging.ILogger;
import de.sirati97.bex_proto.v2.artifcon.ArtifConnection;
import de.sirati97.bex_proto.v2.io.IOHandler;

/**
 * Created by sirati97 on 16.04.2016.
 */
public interface IConnectionFactory<T extends ArtifConnection> {
    T createClient(String connectionName, IOHandler ioHandler);
    T createServer(IOHandler ioHandler);
    AsyncHelper getAsyncHelper();
    ILogger getLogger();
}
