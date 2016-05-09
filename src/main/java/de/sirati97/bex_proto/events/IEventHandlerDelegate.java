package de.sirati97.bex_proto.events;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by sirati97 on 01.05.2016.
 */
public interface IEventHandlerDelegate {
    void invoke(Event event) throws InvocationTargetException, IllegalAccessException;
    Class<? extends Event> getEventClass();
    EventPriority getPriority();
}
