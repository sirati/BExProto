package de.sirati97.bex_proto.events;

/**
 * Created by sirati97 on 03.03.2017 for BexProto.
 */
public interface IEventDelegateListener<E extends Event> extends Listener {
    EventHandler getEventHandler();
    GenericEventHandler getGenericEventHandler();
    Class<E> getEventClass();
    //Object getLifetime();
    void onEvent(E event);
    String getName();
    boolean autoUnregister();
}
