package de.sirati97.bex_proto.v2;

import de.sirati97.bex_proto.threading.AsyncHelper;
import de.sirati97.bex_proto.util.logging.ILogger;
import de.sirati97.bex_proto.v2.service.basic.BasicService;
import de.sirati97.bex_proto.v2.io.IOHandler;

/**
 * Created by sirati97 on 16.04.2016.
 */
public interface IServiceFactory<T extends BasicService> {
    T createClientService(String connectionName, IOHandler ioHandler);
    T createServerService(IOHandler ioHandler);
    AsyncHelper getAsyncHelper();
    ILogger getLogger();
    Class<T> getConnectionClass();
}
