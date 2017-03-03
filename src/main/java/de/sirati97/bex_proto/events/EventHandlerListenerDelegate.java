package de.sirati97.bex_proto.events;

import de.sirati97.bex_proto.events.gen.ClassBuilder;
import de.sirati97.bex_proto.events.gen.GenerateTaskCallback;
import de.sirati97.bex_proto.events.gen.MethodCaller;
import de.sirati97.bex_proto.util.NameReceiver;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by sirati97 on 29.04.2016.
 */
public class EventHandlerListenerDelegate implements IEventHandlerDelegate{
    private final WeakReference<Listener> instance;
    private final Method method;
    private final Class<? extends Event> eventClass;
    private final EventPriority priority;
    private final boolean ignoreCancelled;
    private final GenericEventHandler genericEventHandler;
    private MethodCaller caller;

    public EventHandlerListenerDelegate(WeakReference<Listener> instance, final Method method, Class<? extends Event> eventClass, EventPriority priority, boolean ignoreCancelled, GenericEventHandler genericEventHandler) {
        this.instance = instance;
        this.method = method;
        this.eventClass = eventClass;
        this.priority = priority;
        this.ignoreCancelled = ignoreCancelled;
        this.genericEventHandler = genericEventHandler;
        this.caller = ClassBuilder.INSTANCE.getEventCallerNonBlocking(method, new GenerateTaskCallback() {
            @Override
            public void done(MethodCaller methodCaller) {
                System.out.println("Finished building method caller. name="+methodCaller.getClass().getSimpleName());
                caller = methodCaller;
            }

            @Override
            public void error(Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void invoke(Event event) throws InvocationTargetException, IllegalAccessException {
        Listener listener = instance.get();
        if (listener == null || !checkRaisedEvent(event, ignoreCancelled, genericEventHandler, EventHelperUtil.METHOD_NAME_RECEIVER, method)) {
            return;
        }
        caller.invoke(method, listener, event);
    }

    public Class<? extends Event> getEventClass() {
        return eventClass;
    }

    public EventPriority getPriority() {
        return priority;
    }

    public static <T> boolean checkRaisedEvent(Event event, boolean ignoreCancelled, GenericEventHandler genericEventHandler, NameReceiver<T> nameReceiver, T named) {
        if (event instanceof ICancelable && !ignoreCancelled && ((ICancelable) event).isCancelled()) {
            return false;
        }
        if (event instanceof GenericEvent) {
            Class[] genericsEvent = ((GenericEvent) event).getGenerics();
            Class[] genericsHandler = genericEventHandler.generics();
            if (genericsEvent.length != genericsHandler.length) {
                throw new IllegalStateException("Method " + nameReceiver.getName(named) + " should have " + genericsEvent.length + " generics, but has " + genericsHandler.length + " generics instant");
            }

            for (int i = 0; i < genericsEvent.length; i++) {
                //noinspection unchecked
                if (!genericsHandler[i].isAssignableFrom(genericsEvent[i])) {
                    return false;
                }
            }
        }
        return true;
    }
}
