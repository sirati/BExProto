package de.sirati97.bex_proto.events;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by sirati97 on 09.05.2016.
 */
public class EventHelperUtil {
    private EventHelperUtil() {throw new UnsupportedOperationException();}
    private static final Set<EventClassHelper> eventClassHelpers = new HashSet<>(1);
    static {
        eventClassHelpers.add(JavaEventClassHelperImpl.INSTANCE);
    }

    private static RuntimeException create_EventDistributorMissingException(Class<? extends Event> clazz, EventClassHelper helper) {
        return new EventDistributorMissingException(helper.getEventDistributorMissingMessage(clazz));
    }

    private static RuntimeException create_GenericSuperclassesMissingException(Class<? extends Event> clazz, EventClassHelper helper) {
        return new GenericSuperclassesMissingException(helper.getGenericSuperclassesMissingMessage(clazz));
    }

    private static RuntimeException create_MalformedListenerClassException(Class<? extends Listener> clazz, EventClassHelper helper) {
        return new MalformedListenerClassException(helper.getMalformedListenerClassMessage(clazz));
    }

    private static RuntimeException create_CannotFindMatchingEventClassHelperException_ForEvent(Class<? extends Event> clazz) {
        return new CannotFindMatchingEventClassHelperException("Cannot find class helper for Event " + clazz.getName());
    }

    private static RuntimeException create_CannotFindMatchingEventClassHelperException_ForListener(Class<? extends Listener> clazz) {
        return new CannotFindMatchingEventClassHelperException("Cannot find class helper for Listener " + clazz.getName());
    }


    public static EventDistributor getEventDistributor(Class<? extends Event> eventClass) {
        EventClassHelper handler = null;
        for (EventClassHelper helper:eventClassHelpers) {
            if (helper.isHandlerForEventClass(eventClass)) {
                handler = helper;
                EventDistributor result = helper.getEventDistributor(eventClass);
                if (result!=null) {
                    return result;
                }
            }
        }
        if (handler == null) {
            throw create_CannotFindMatchingEventClassHelperException_ForEvent(eventClass);
        } else {
            throw create_EventDistributorMissingException(eventClass, handler);
        }
    }

    public static Class[] getEventGenericsSuperclasses(Class<? extends Event> eventClass) {
        EventClassHelper handler = null;
        for (EventClassHelper helper:eventClassHelpers) {
            if (helper.isHandlerForEventClass(eventClass)) {
                handler = helper;
                Class[] result = helper.getEventGenericsSuperclasses(eventClass);
                if (result!=null) {
                    return result;
                }
            }
        }
        if (handler == null) {
            throw create_CannotFindMatchingEventClassHelperException_ForEvent(eventClass);
        } else {
            throw create_GenericSuperclassesMissingException(eventClass, handler);
        }
    }

    public static GenericEventHandler getGenericEventHandlerByMethod(Class<? extends Event> eventClass, Method method) {
        EventClassHelper handler = null;
        for (EventClassHelper helper:eventClassHelpers) {
            if (helper.isHandlerForEventClass(eventClass)) {
                handler = helper;
                GenericEventHandler result = helper.getGenericEventHandlerByMethod(eventClass, method);
                if (result!=null) {
                    return result;
                }
            }
        }
        if (handler == null) {
            throw create_CannotFindMatchingEventClassHelperException_ForEvent(eventClass);
        } else {
            return null;
        }
    }

    public static void checkListenerClass(Class<? extends Listener> listenerClass) {
        EventClassHelper handler = null;
        for (EventClassHelper helper:eventClassHelpers) {
            if (helper.isHandlerForListenerClass(listenerClass)) {
                handler = helper;
                if (helper.checkListenerClass(listenerClass)) {
                    return;
                }
            }
        }
        if (handler == null) {
            throw create_CannotFindMatchingEventClassHelperException_ForListener(listenerClass);
        } else {
            throw create_MalformedListenerClassException(listenerClass, handler);
        }
    }

    public static void registerEventClassHelper(EventClassHelper eventClassHelper) {
        eventClassHelpers.add(eventClassHelper);
    }
}
