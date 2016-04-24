package de.sirati97.bex_proto.v2;

import de.sirati97.bex_proto.util.logging.ILogger;
import de.sirati97.bex_proto.v2.artifcon.ArtifConnection;

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
}