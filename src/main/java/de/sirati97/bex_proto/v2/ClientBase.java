package de.sirati97.bex_proto.v2;

import de.sirati97.bex_proto.events.Event;
import de.sirati97.bex_proto.events.EventRegister;
import de.sirati97.bex_proto.events.Listener;
import de.sirati97.bex_proto.util.logging.ILogger;
import de.sirati97.bex_proto.v2.artifcon.ArtifConnection;

import java.util.HashSet;

/**
 * Created by sirati97 on 17.04.2016.
 */
public abstract class ClientBase<Connection extends ArtifConnection> extends CHBase<Connection> {
    private Connection connection;
    private final String name;

    public ClientBase(IConnectionFactory<Connection> factory, String name) {
        super(factory);
        this.name = name;
    }

    protected void setConnection(Connection connection) {
        this.connection = connection;
        if (connection != null) {
            registerConnection(connection);
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean isConnected() {
        return connection!=null&&connection.isConnected();
    }

    @Override
    protected ILogger getLogger() {
        return connection==null?getFactory().getLogger():connection.getLogger();
    }

    @Override
    public boolean registerEventListener(Listener listener) {
        return getEventRegister().registerEventListener(listener);
    }

    @Override
    public boolean unregisterEventListener(Listener listener) {
        return getEventRegister().unregisterEventListener(listener);
    }

    @Override
    public void invokeEvent(Event event) {
        getEventRegister().invokeEvent(event);
    }

    public EventRegister getEventRegister() {
        return connection.getEventRegister();
    }


    @Override
    public synchronized void disconnect() {
        if (getConnection().isConnected()) {
            getConnection().disconnect();
        }
    }

    @Override
    public synchronized void stop() {
        disconnect();
        for (Connection connection:new HashSet<>(getConnections())) {
            if (connection.isConnected()) {
                connection.disconnect();
            }
        }
    }
}
