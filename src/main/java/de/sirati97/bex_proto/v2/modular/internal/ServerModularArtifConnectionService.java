package de.sirati97.bex_proto.v2.modular.internal;

import de.sirati97.bex_proto.util.IServerConnection;
import de.sirati97.bex_proto.v2.io.IOHandler;
import de.sirati97.bex_proto.v2.modular.ModularArtifConnectionService;
import de.sirati97.bex_proto.v2.modular.ModuleHandler;

/**
 * Created by sirati97 on 13.04.2016.
 */
public class ServerModularArtifConnectionService extends ModularArtifConnectionService implements IServerConnection {
    public ServerModularArtifConnectionService(IOHandler ioHandler, ModuleHandler moduleHandler) {
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
