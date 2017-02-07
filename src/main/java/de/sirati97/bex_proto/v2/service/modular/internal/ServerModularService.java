package de.sirati97.bex_proto.v2.service.modular.internal;

import de.sirati97.bex_proto.util.IServerConnection;
import de.sirati97.bex_proto.v2.io.IOHandler;
import de.sirati97.bex_proto.v2.service.modular.ModularService;
import de.sirati97.bex_proto.v2.service.modular.ModuleHandler;

/**
 * Created by sirati97 on 13.04.2016.
 */
public class ServerModularService extends ModularService implements IServerConnection {
    public ServerModularService(IOHandler ioHandler, ModuleHandler moduleHandler) {
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
