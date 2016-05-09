package de.sirati97.bex_proto.events;

import java.lang.reflect.Method;

/**
 * Created by sirati97 on 09.05.2016.
 */
public interface EventClassHelper {
    EventDistributor getEventDistributor(Class<? extends Event> eventClass);
    Class[] getEventGenericsSuperclasses(Class<? extends Event> eventClass);
    boolean isHandlerForEventClass(Class<? extends Event> eventClass);
    boolean isHandlerForListenerClass(Class<? extends Listener> listenerClass);
    GenericEventHandler getGenericEventHandlerByMethod(Class<? extends Event> eventClass, Method method);
    String getEventDistributorMissingMessage(Class<? extends Event> eventClass);
    String getGenericSuperclassesMissingMessage(Class<? extends Event> eventClass);
    String getMalformedListenerClassMessage(Class<? extends Listener> listenerClass);
    boolean checkListenerClass(Class<? extends Listener> listenerClass);
}
