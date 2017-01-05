package de.sirati97.bex_proto.v2.networkmodell;

import de.sirati97.bex_proto.threading.AsyncHelper;
import de.sirati97.bex_proto.util.logging.ILogger;
import de.sirati97.bex_proto.v2.IConnectionServiceFactory;
import de.sirati97.bex_proto.v2.artifcon.ArtifConnectionService;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static de.sirati97.bex_proto.v2.networkmodell.CommonArchitectureFunction.Peer;

/**
 * Created by sirati97 on 17.04.2016.
 */
public abstract class ConnectionBase<Connection extends ArtifConnectionService> implements IConnection<Connection> {
    private final IConnectionServiceFactory<Connection> factory;
    private final Set<Connection> connections = new HashSet<>();
    private final Set<Connection> connectionsReadOnly = Collections.unmodifiableSet(connections);

    public ConnectionBase(IConnectionServiceFactory<Connection> factory) {
        this.factory = factory;
    }

    protected abstract ILogger getLogger();

    protected IConnectionServiceFactory<Connection> getFactory() {
        return factory;
    }

    @Override
    public AsyncHelper getAsyncHelper() {
        return getFactory().getAsyncHelper();
    }

    protected void registerConnection(Connection connection) {
        connections.add(connection);
    }

    protected void unregisterConnection(Connection connection) {
        connections.remove(connection);
    }

    @Override
    public Set<Connection> getConnections() {
        return connectionsReadOnly;
    }

    @Override
    public IArchitectureFunction getArchitectureFunction() {
        return Peer;
    }
}
