package de.sirati97.bex_proto.events;

import de.sirati97.bex_proto.util.keying.Key;
import de.sirati97.bex_proto.util.keying.KeyEnvironment;

/**
 * Created by sirati97 on 29.04.2016.
 */
public interface EventDistributor {
    KeyEnvironment ENVIRONMENT = new KeyEnvironment();
    void distribute(Event event, EventRegister register);
    Object getProperty(Key key);
    Object removeProperty(Key key);
    void setProperty(Key key, Object value);
}
