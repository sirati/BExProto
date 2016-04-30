package de.sirati97.bex_proto.events;

/**
 * Created by sirati97 on 29.04.2016.
 */
public interface IEventRegister {
    boolean registerEventListener(Listener listener);
    boolean unregisterEventListener(Listener listener);
    void invokeEvent(Event event);
}
