package de.sirati97.bex_proto.util;

import de.sirati97.bex_proto.datahandler.SendStream;

/**
 * Created by sirati97 on 15.03.2016.
 */
public interface IConnection {
    void send(SendStream stream);
}
