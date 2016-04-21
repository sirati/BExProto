package de.sirati97.bex_proto.v2;

import de.sirati97.bex_proto.threading.AsyncHelper;
import de.sirati97.bex_proto.util.logging.ILogger;
import de.sirati97.bex_proto.v2.artifcon.ArtifConnection;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeoutException;

/**
 * Created by sirati97 on 17.04.2016.
 */
public abstract class CHBase<Connection extends ArtifConnection> {
    private final IConnectionFactory<Connection> factory;
    private final Set<Connection> connections = new HashSet<>();
    private final Set<Connection> connectionsReadOnly = Collections.unmodifiableSet(connections);

    public CHBase(IConnectionFactory<Connection> factory) {
        this.factory = factory;
    }

    public abstract void connect() throws InterruptedException, IOException, TimeoutException;
    public abstract void disconnect();
    public abstract void stop();
    public abstract boolean isConnected();
    protected abstract ILogger getLogger();

    protected IConnectionFactory<Connection> getFactory() {
        return factory;
    }

    public AsyncHelper getAsyncHelper() {
        return getFactory().getAsyncHelper();
    }

    protected void registerConnection(Connection connection) {
        connections.add(connection);
    }

    protected void unregisterConnection(Connection connection) {
        connections.remove(connection);
    }

    public Set<Connection> getConnections() {
        return connectionsReadOnly;
    }
}
