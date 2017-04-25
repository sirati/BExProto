package de.sirati97.bex_proto.v2.events;

import de.sirati97.bex_proto.events.EventDistributor;
import de.sirati97.bex_proto.events.EventDistributorImpl;
import de.sirati97.bex_proto.events.GenericEvent;
import de.sirati97.bex_proto.v2.service.basic.BasicService;
import de.sirati97.bex_proto.v2.service.basic.DisconnectReason;

/**
 * Created by sirati97 on 25.04.2017 for BexProto.
 */
public class ConnectionClosedEvent<Connection extends BasicService> implements GenericEvent {
    private static final EventDistributor DISTRIBUTOR = new EventDistributorImpl();
    private static final Class[] GENERIC_SUPERCLASSES = {BasicService.class};
    public static EventDistributor getEventDistributor() {
        return DISTRIBUTOR;
    }
    public static Class[] getGenericSuperclasses() {
        return GENERIC_SUPERCLASSES;
    }

    public ConnectionClosedEvent(BasicService connection, DisconnectReason reason, Throwable error, Class<Connection> connectionClass) {
        //noinspection unchecked
        this.connection = (Connection)connection;
        this.reason = reason;
        this.error = error;
        this.generics = new Class[]{connectionClass};
    }


    private final Connection connection;
    private final DisconnectReason reason;
    private final Throwable error;
    private final Class[] generics;

    public Connection getConnection() {
        return connection;
    }

    @Override
    public Class[] getGenerics() {
        return generics;
    }

    public DisconnectReason getReason() {
        return reason;
    }

    /**
     *
     * @return error or null when different reason for disconnect
     */
    public Throwable getError() {
        return error;
    }
}
