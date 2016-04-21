package de.sirati97.bex_proto.util;

import de.sirati97.bex_proto.datahandler.SendStream;
import de.sirati97.bex_proto.util.logging.ILogger;

/**
 * Created by sirati97 on 15.03.2016.
 */
public interface IConnection {
    void send(SendStream stream, boolean reliable);
    void send(SendStream stream);
    ILogger getLogger();
}
