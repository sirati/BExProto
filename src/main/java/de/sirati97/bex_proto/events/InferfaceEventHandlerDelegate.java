package de.sirati97.bex_proto.events;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by sirati97 on 29.04.2016.
 */
public class InferfaceEventHandlerDelegate {
    private final WeakReference<Listener> instance;
    private final Class<? extends Event> eventClass;
    private final EventPriority priority;
    private final boolean ignoreCancelled;
    private final GenericEventHandler genericEventHandler;

    public InferfaceEventHandlerDelegate(WeakReference<Listener> instance, Class<? extends Event> eventClass, EventPriority priority, boolean ignoreCancelled, GenericEventHandler genericEventHandler) {
        this.instance = instance;
        this.eventClass = eventClass;
        this.priority = priority;
        this.ignoreCancelled = ignoreCancelled;
        this.genericEventHandler = genericEventHandler;
    }

    public void invoke(Event event) throws InvocationTargetException, IllegalAccessException {
        EventListener listener = (EventListener) instance.get();
        if (listener == null ||
           (event instanceof Cancelable && !ignoreCancelled && ((Cancelable) event).isCancelled())) {
            return;
        }
        if (event instanceof GenericEvent) {
            Class[] genericsEvent = ((GenericEvent) event).getGenerics();
            Class[] genericsHandler = genericEventHandler.generics();
            if (genericsEvent.length != genericsHandler.length) {
                return;
            }

            for (int i = 0; i < genericsEvent.length; i++) {
                //noinspection unchecked
                if (!genericsHandler[i].isAssignableFrom(genericsEvent[i])) {
                    return;
                }
            }
        }
        listener.onEvent(event, eventClass);
    }

    public Class<? extends Event> getEventClass() {
        return eventClass;
    }

    public EventPriority getPriority() {
        return priority;
    }
}
