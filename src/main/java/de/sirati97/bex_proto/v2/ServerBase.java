package de.sirati97.bex_proto.v2;

import de.sirati97.bex_proto.events.Event;
import de.sirati97.bex_proto.events.EventRegister;
import de.sirati97.bex_proto.events.Listener;
import de.sirati97.bex_proto.util.logging.ILogger;
import de.sirati97.bex_proto.v2.artifcon.ArtifConnection;
import de.sirati97.bex_proto.v2.events.NewConnectionEvent;

import java.io.IOException;

/**
 * Created by sirati97 on 17.04.2016.
 */
public abstract class ServerBase<Connection extends ArtifConnection> extends CHBase<Connection> {
    private ILogger logger;
    private EventRegister eventRegister;

    public ServerBase(IConnectionFactory<Connection> factory) {
        super(factory);
    }

    public abstract void startListening() throws IOException;
    public abstract void stopListening();
    public abstract boolean isListening();

    /**wraps isListening*/
    @Override
    public final boolean isConnected() {
        return isListening();
    }

    /**wraps startListening*/
    @Override
    public final void connect() {
        stopListening();
    }


    /**wraps stopListening*/
    @Override
    public final void disconnect() {
        stopListening();
    }

    @Override
    protected final ILogger getLogger() {
        return logger==null?(logger=createLogger()):logger;
    }
    protected abstract ILogger createLogger();

    @Override
    protected void registerConnection(Connection connection) {
        super.registerConnection(connection);
        connection.getEventRegister().addParent(getEventRegister());
        invokeEvent(new NewConnectionEvent<>(connection, getFactory().getConnectionClass()));
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
        if (eventRegister == null) {
            eventRegister = new EventRegister(getLogger());
        }
        return eventRegister;
    }

    @Override
    public EventRegister getEventRegisterImplementation() {
        return eventRegister;
    }
}
