package de.sirati97.bex_proto.events;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Created by sirati97 on 09.05.2016.
 */
public class JavaEventClassHelperImpl extends EventClassHelperBase {
    public final static EventClassHelper INSTANCE = new JavaEventClassHelperImpl();

    private JavaEventClassHelperImpl() {}

    @Override
    public EventDistributor getEventDistributor(Class<? extends Event> eventClass) {
        try {
            Method getDistributor = eventClass.getDeclaredMethod("getEventDistributor");
            int modifiers = getDistributor.getModifiers();
            if (!Modifier.isStatic(modifiers) || !Modifier.isPublic(modifiers)) {
                return null;
            }
            return (EventDistributor) getDistributor.invoke(null);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            return null;
        }
    }

    @Override
    public Class[] getEventGenericsSuperclasses(Class<? extends Event> eventClass) {
        try {
            Method getGenericsSuperclasses = eventClass.getDeclaredMethod("getGenericSuperclasses");
            int modifiers = getGenericsSuperclasses.getModifiers();
            if (!Modifier.isStatic(modifiers) || !Modifier.isPublic(modifiers)) {
                return null;
            }
            return (Class[]) getGenericsSuperclasses.invoke(null);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            return null;
        }
    }

    @Override
    public boolean isHandlerForEventClass(Class<? extends Event> eventClass) {
        return !NonJavaEvent.class.isAssignableFrom(eventClass);
    }

    @Override
    public boolean isHandlerForListenerClass(Class<? extends Listener> listenerClass) {
        return !NonJavaListener.class.isAssignableFrom(listenerClass);
    }

    @Override
    public GenericEventHandler getGenericEventHandlerByMethod(Class<? extends Event> eventClass, Method method) {
        return null;
    }

    @Override
    public boolean checkListenerClass(Class<? extends Listener> listenerClass) {
        return true;
    }

    @Override
    public String getMalformedListenerClassMessage(Class<? extends Listener> listenerClass) {
        return "Listener " + listenerClass.getName() + " has a malformed structure. This exception should not been thrown in a unmodded environment";
    }

    @Override
    public String getEventDistributorMissingMessage(Class<? extends Event> eventClass) {
        return "Event " + eventClass.getName() + " must have 'public static EventDistributor getEventDistributor(){...}'";
    }

    @Override
    public String getGenericSuperclassesMissingMessage(Class<? extends Event> eventClass) {
        return "Event " + eventClass.getName() + " must have 'public static Class[] getGenericsSuperclasses(){...}'";
    }
}
