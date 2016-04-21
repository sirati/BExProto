package de.sirati97.bex_proto.v2;

import de.sirati97.bex_proto.util.logging.ILogger;
import de.sirati97.bex_proto.v2.artifcon.ArtifConnection;

/**
 * Created by sirati97 on 17.04.2016.
 */
public abstract class ServerBase<Connection extends ArtifConnection> extends CHBase<Connection> {
    private ILogger logger;

    public ServerBase(IConnectionFactory<Connection> factory) {
        super(factory);
    }

    public abstract void startListening();
    public abstract void stopListening();
    public abstract boolean isListening();

    /**equal to isListening*/
    @Override
    public final boolean isConnected() {
        return isListening();
    }

    /**equal to startListening*/
    @Override
    public final void connect() {
        stopListening();
    }


    /**equal to stopListening*/
    @Override
    public final void disconnect() {
        stopListening();
    }

    @Override
    protected final ILogger getLogger() {
        return logger==null?(logger=createLogger()):logger;
    }
    protected abstract ILogger createLogger();
}
