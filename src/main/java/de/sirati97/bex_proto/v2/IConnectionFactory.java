package de.sirati97.bex_proto.v2;

import de.sirati97.bex_proto.v2.artifcon.ArtifConnection;
import de.sirati97.bex_proto.v2.artifcon.IOHandler;

/**
 * Created by sirati97 on 16.04.2016.
 */
public interface IConnectionFactory<T extends ArtifConnection> {
    T createClient(String connectionName, IOHandler ioHandler);
    T createServer(IOHandler ioHandler);
}
