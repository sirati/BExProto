package de.sirati97.bex_proto.v2.networkmodell;

import de.sirati97.bex_proto.events.Event;
import de.sirati97.bex_proto.events.EventRegister;
import de.sirati97.bex_proto.events.IEventRegister;
import de.sirati97.bex_proto.events.Listener;
import de.sirati97.bex_proto.threading.IAsyncHelper;
import de.sirati97.bex_proto.v2.service.basic.BasicService;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.TimeoutException;

/**
 * Created by sirati97 on 04.01.2017 for BexProto.
 */
public interface IConnection<Connection extends BasicService> extends IEventRegister {
    void connect() throws InterruptedException, IOException, TimeoutException;

    void disconnect();

    void stop();

    boolean isConnected();

    IAsyncHelper getAsyncHelper();

    Set<Connection> getConnections();

    boolean registerEventListener(Listener listener);

    boolean unregisterEventListener(Listener listener);

    void invokeEvent(Event event);

    EventRegister getEventRegister();

    EventRegister getEventRegisterImplementation();

    INetworkProtocol getUnderlyingProtocol();

    INetworkStackImplementation getNetworkStackImplementation();

    IArchitectureFunction getArchitectureFunction();
}
