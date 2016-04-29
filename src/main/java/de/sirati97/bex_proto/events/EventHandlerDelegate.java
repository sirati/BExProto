package de.sirati97.bex_proto.events;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by sirati97 on 29.04.2016.
 */
public class EventHandlerDelegate {
    private final WeakReference<Listener> instance;
    private final Method method;
    public final Class<? extends Event> eventClass;
    public final EventPriority priority;
    public final boolean ignoreCancelled;


    public EventHandlerDelegate(WeakReference<Listener> instance, Method method, Class<? extends Event> eventClass, EventPriority priority, boolean ignoreCancelled) {
        this.instance = instance;
        this.method = method;
        this.eventClass = eventClass;
        this.priority = priority;
        this.ignoreCancelled = ignoreCancelled;
    }

    public void invoke(Event event) throws InvocationTargetException, IllegalAccessException {
        if (instance.get() == null ||
           (event instanceof Cancelable && !ignoreCancelled && ((Cancelable) event).isCancelled())) {
            return;
        }
        boolean not_accessible = !method.isAccessible();
        if (not_accessible) {
            method.setAccessible(true);
        }
        method.invoke(instance.get(), event);
        if (not_accessible) {
            method.setAccessible(false);
        }
    }
}
