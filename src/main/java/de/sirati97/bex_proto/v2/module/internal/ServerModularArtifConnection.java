package de.sirati97.bex_proto.v2.module.internal;

import de.sirati97.bex_proto.util.IServerConnection;
import de.sirati97.bex_proto.v2.artifcon.IOHandler;
import de.sirati97.bex_proto.v2.module.ModularArtifConnection;
import de.sirati97.bex_proto.v2.module.ModuleHandler;

/**
 * Created by sirati97 on 13.04.2016.
 */
public class ServerModularArtifConnection extends ModularArtifConnection implements IServerConnection {
    public ServerModularArtifConnection(IOHandler ioHandler, ModuleHandler moduleHandler) {
        super("NewConnection", ioHandler, moduleHandler);
    }

    @Override
    public void setConnectedWith(String connectedWith) {
        if (getConnectionName().equals("NewConnection")) {
            setConnectionName(connectedWith);
        }
        super.setConnectedWith(connectedWith);
    }
}
