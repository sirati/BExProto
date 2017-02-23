package de.sirati97.bex_proto.v2.networkmodell;

import de.sirati97.bex_proto.threading.IAsyncHelper;
import de.sirati97.bex_proto.util.logging.ILogger;
import de.sirati97.bex_proto.v2.IServiceFactory;
import de.sirati97.bex_proto.v2.service.basic.BasicService;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static de.sirati97.bex_proto.v2.networkmodell.CommonArchitectureFunction.Peer;

/**
 * Created by sirati97 on 17.04.2016.
 */
public abstract class ConnectionBase<Connection extends BasicService> implements IConnection<Connection> {
    private final IServiceFactory<Connection> factory;
    private final Set<Connection> connections = new HashSet<>();
    private final Set<Connection> connectionsReadOnly = Collections.unmodifiableSet(connections);

    public ConnectionBase(IServiceFactory<Connection> factory) {
        this.factory = factory;
    }

    protected abstract ILogger getLogger();

    protected IServiceFactory<Connection> getFactory() {
        return factory;
    }

    @Override
    public IAsyncHelper getAsyncHelper() {
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
