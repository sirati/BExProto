package de.sirati97.bex_proto.events;

import de.sirati97.bex_proto.events.gen.ClassBuilder;
import de.sirati97.bex_proto.events.gen.GenerateTaskCallback;
import de.sirati97.bex_proto.events.gen.MethodCaller;

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
    private final GenericEventHandler genericEventHandler;
    private MethodCaller caller;

    public EventHandlerDelegate(WeakReference<Listener> instance, final Method method, Class<? extends Event> eventClass, EventPriority priority, boolean ignoreCancelled, GenericEventHandler genericEventHandler) {
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
        if (instance.get() == null ||
           (event instanceof Cancelable && !ignoreCancelled && ((Cancelable) event).isCancelled())) {
            return;
        }
        if (event instanceof GenericEvent) {
            Class[] genericsEvent = ((GenericEvent) event).getGenerics();
            Class[] genericsHandler = genericEventHandler.generics();
            if (genericsEvent.length != genericsHandler.length) {
                throw new IllegalStateException("Method " + method.getName() + " should have " + genericsEvent.length + " generics, but has " + genericsHandler.length + " generics instant");
            }

            for (int i = 0; i < genericsEvent.length; i++) {
                //noinspection unchecked
                if (!genericsHandler[i].isAssignableFrom(genericsEvent[i])) {
                    return;
                }
            }
        }
        caller.invoke(method, instance.get(), event);
    }
}
