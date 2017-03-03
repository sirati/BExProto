package de.sirati97.bex_proto.events;

import de.sirati97.bex_proto.util.keying.Key;
import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;

/**
 * Created by sirati97 on 29.04.2016.
 */
public class EventDistributorImpl implements EventDistributor {
    private final TIntObjectMap<Object> properties= new TIntObjectHashMap<>();
    @Override
    public void distribute(Event event, EventRegister register) {
        register.invokeDelegates(event, register.getDelegates(event.getClass()));
    }

    @Override
    public Object getProperty(Key key) {
        key.checkEnvironment(ENVIRONMENT);
        return properties.get(key.getId());
    }

    @Override
    public Object removeProperty(Key key) {
        key.checkEnvironment(ENVIRONMENT);
        return properties.remove(key.getId());
    }

    @Override
    public void setProperty(Key key, Object value) {
        key.checkEnvironment(ENVIRONMENT);
        properties.put(key.getId(), value);
    }
}
