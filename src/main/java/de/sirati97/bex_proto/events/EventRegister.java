package de.sirati97.bex_proto.events;

import de.sirati97.bex_proto.util.logging.ILogger;

import java.lang.annotation.Annotation;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by sirati97 on 29.04.2016.
 */
public class EventRegister implements IEventRegister{
    private final Map<Class<? extends Event>, EventHandlerDelegateSet> delegates = new HashMap<>();
    private final Map<WeakReference<? extends Listener>, Set<EventHandlerDelegate>> references = new HashMap<>();
    private final Set<WeakReference<? extends Listener>> listeners = new HashSet<>();
    private final ReferenceQueue<Listener> referenceQueue = new ReferenceQueue<>();
    private final Set<EventRegister> parents = new HashSet<>();
    private final ILogger logger;
    private final boolean cancelEventOnError;

    public EventRegister(ILogger logger) {
        this(logger, false);
    }

    public EventRegister(ILogger logger, boolean cancelEventOnError) {
        this.cancelEventOnError = cancelEventOnError;
        this.logger = logger.getLogger(logger.getPrefix()+"|EventRegister");
    }


    public boolean registerEventListener(Listener listener) {
        cleanup();
        for (WeakReference reference:listeners) {
            if (listener == reference.get()) {
                throw new IllegalStateException("This listener is already listening");
            }
        }
        Class<? extends Listener> clazz = listener.getClass();
        WeakReference<Listener> instance = new WeakReference<>(listener, referenceQueue);
        Set<EventHandlerDelegate> referencesSet = new HashSet<>();

        Set<Method> methods = new LinkedHashSet<>(Arrays.asList(clazz.getMethods()));
        methods.addAll(Arrays.asList(clazz.getDeclaredMethods()));

        boolean result = registerMethods(instance, referencesSet, methods)>0;

        if (result) {
            listeners.add(instance);
            references.put(instance, referencesSet);
        } else {
            instance.clear();
        }
        return result;
    }

    private int registerMethods(WeakReference<Listener> instance, Set<EventHandlerDelegate> referencesSet, Set<Method> methods) {
        int found = 0;
        method_for: for (Method method:methods) {
            EventHandler eventHandler = null;
            GenericEventHandler genericEventHandler = null;
            for (Annotation annotation: method.getAnnotations()) {
                if (annotation instanceof EventHandler) {
                    eventHandler = (EventHandler) annotation;
                }
                if (annotation instanceof GenericEventHandler) {
                    genericEventHandler = (GenericEventHandler) annotation;
                }
                if (eventHandler!=null&&genericEventHandler!=null) {
                    break;
                }
            }
            if (eventHandler != null) {
                registerMethod(instance, referencesSet, eventHandler, genericEventHandler, method);
                found++;
            } else if( genericEventHandler!=null) {
                throw new IllegalStateException("Method " + method.getName() + " has annotation @GenericEventHandler without having @EventHandler as well.");
            }
        }
        return found;
    }

    private void registerMethod(WeakReference<Listener> instance, Set<EventHandlerDelegate> referencesSet, EventHandler eventHandler, GenericEventHandler genericEventHandler, Method method) {
        Class[] parameter = method.getParameterTypes();
        if (parameter.length!=1) {
            throw new IllegalStateException("Method " + method.getName() + " has to have only one parameter");
        }
        if (!Event.class.isAssignableFrom(parameter[0])) {
            throw new IllegalStateException("Method " + method.getName() + " parameter has to be a subclass of " + Event.class.getName());
        }
        //noinspection unchecked
        Class<? extends Event> eventClass = parameter[0];
        if (genericEventHandler != null) {
            if (!GenericEvent.class.isAssignableFrom(eventClass)) {
                throw new IllegalStateException("Method " + method.getName() + " has annotation @GenericEventHandler while parameter is not a generic event.");
            }
            Method getGenericsSuperclasses;
            try {
                getGenericsSuperclasses = eventClass.getDeclaredMethod("getGenericsSuperclasses");
            } catch (NoSuchMethodException e) {
                throw createEventDistributorMissingException(eventClass);
            }
            int modifiers = getGenericsSuperclasses.getModifiers();
            if (!Modifier.isStatic(modifiers) || !Modifier.isPublic(modifiers)) {
                throw createEventDistributorMissingException(eventClass);
            }
            Class[] genericsSuperclasses;
            try {
                genericsSuperclasses = (Class[]) getGenericsSuperclasses.invoke(null);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new IllegalStateException("Could not obtain generic superclasses");
            }
            Class[] genericsHandler = genericEventHandler.generics();
            if (genericsSuperclasses.length != genericsHandler.length) {
                throw new IllegalStateException("Method " + method.getName() + " should have " + genericsSuperclasses.length + " generics, but has " + genericsHandler.length + " generics instant");
            }

            for (int i = 0; i < genericsSuperclasses.length; i++) {
                //noinspection unchecked
                if (!genericsSuperclasses[i].isAssignableFrom(genericsHandler[i])) {
                    throw new IllegalStateException("Generic " + (i+1) + " has to be a subclass of" + genericsSuperclasses[i].getName());
                }
            }
        }
        EventHandlerDelegate delegate = new EventHandlerDelegate(instance, method, eventClass, eventHandler.priority(), eventHandler.ignoreCancelled(), genericEventHandler);

        EventHandlerDelegateSet set = delegates.get(eventClass);
        if (set == null) {
            set = new EventHandlerDelegateSet();
            delegates.put(eventClass, set);
        }
        set.add(delegate);
        referencesSet.add(delegate);
    }

    private void cleanup() {
        WeakReference<? extends Listener> reference;
        //noinspection unchecked
        while ((reference= (WeakReference<? extends Listener>) referenceQueue.poll())!=null) {
            unregister(reference);
        }

    }

    private void unregister(WeakReference<? extends Listener> reference) {
        listeners.remove(reference);
        Set<EventHandlerDelegate> delegatesSet = references.remove(reference);
        for (EventHandlerDelegate delegate:delegatesSet) {
            EventHandlerDelegateSet classDelegates = delegates.get(delegate.eventClass);
            classDelegates.remove(delegate);
            if (classDelegates.size() == 0) {
                delegates.remove(delegate.eventClass);
            }
        }
    }

    public boolean unregisterEventListener(Listener listener) {
        cleanup();
        for (WeakReference<? extends Listener> reference:listeners) {
            if (listener == reference.get()) {
                unregister(reference);
                return  true;
            }
        }
        return false;
    }

    private IllegalStateException createEventDistributorMissingException(Class<? extends Event> clazz) {
        return new IllegalStateException("Event " + clazz.getName() + " need to has 'public static EventDistributor getEventDistributor();'");
    }

    private IllegalStateException createGenericSuperclassesMissingException(Class<? extends Event> clazz) {
        return new IllegalStateException("Event " + clazz.getName() + " need to has 'public static Class[] getGenericsSuperclasses();'");
    }

    public void invokeEvent(Event event) {
        cleanup();
        Class<? extends Event> eventClass= event.getClass();
        Method getDistributor;
        try {
            getDistributor = eventClass.getDeclaredMethod("getEventDistributor");
        } catch (NoSuchMethodException e) {
            throw createEventDistributorMissingException(eventClass);
        }
        int modifiers = getDistributor.getModifiers();
        if (!Modifier.isStatic(modifiers) || !Modifier.isPublic(modifiers)) {
            throw createEventDistributorMissingException(eventClass);
        }
        EventDistributor distributor;
        try {
            distributor = (EventDistributor) getDistributor.invoke(null);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException("Could not obtain EventDistributor");
        }
        distributor.distribute(event, this);
    }

    public Set<EventHandlerDelegateSet> getDelegates(Class<? extends Event> eventClass) {
        Set<EventHandlerDelegateSet> result = new HashSet<>(parents.size()+1);
        getDelegates(eventClass,result);
        return result;
    }


    public void getDelegates(Class<? extends Event> eventClass, Set<EventHandlerDelegateSet> result) {
        for (EventRegister parent:parents) {
            parent.getDelegates(eventClass, result);
        }
        EventHandlerDelegateSet set = delegates.get(eventClass);
        if (set != null) {
            result.add(set);
        }
    }


    public void invokeDelegates(Event event, Set<EventHandlerDelegateSet> delegates) {
        for (EventPriority priority:EventPriority.values()) {
            for (EventHandlerDelegateSet set:delegates) {
                set.invoke(event, this, priority);
            }
        }
    }

    void _invokeDelegates(Event event, Set<EventHandlerDelegate> delegates) {
        String exceptionString=null;
        for (EventHandlerDelegate delegate:delegates) {
            try {
                delegate.invoke(event);
            } catch (IllegalAccessException e) {
                throw new IllegalStateException("Could not invoke event handler", e);
            } catch (Throwable t) {
                if (t instanceof InvocationTargetException) {
                    t = t.getCause();
                }
                if (exceptionString == null) {
                    exceptionString = "Exception thrown while invoking EventHandler for " + event.getClass().getName();
                }
                if (cancelEventOnError) {
                    throw new IllegalStateException(exceptionString, t);
                } else {
                    logger.error(exceptionString, t);
                }
            }
        }
    }

    public boolean isParent(EventRegister register) {
        if (parents.contains(register)) {
            return true;
        }
        for (EventRegister parent:parents) {
            if (parent.isParent(register)) {
                return true;
            }
        }
        return false;
    }

    public boolean addParent(EventRegister register) {
        if (register.isParent(this)) {
            throw new IllegalStateException("You are not allowed to set a child as parent");
        }
        if (isParent(register)) {
            return false;
        }
        parents.add(register);
        return true;
    }

    public void removeParent(EventRegister register) {
        parents.remove(register);
    }

    public void setLoggerPrefix(String prefix) {
        logger.changePrefix(prefix+"|EventRegister");
    }
}
