package de.sirati97.bex_proto.events;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by sirati97 on 29.04.2016.
 */
public class EventHandlerDelegateSet {
    private final EnumMap<EventPriority, Set<EventHandlerDelegate>> delegates = new EnumMap<EventPriority, Set<EventHandlerDelegate>>(EventPriority.class);
    private int size=0;

    public void add(EventHandlerDelegate delegate) {
        Set<EventHandlerDelegate> set = delegates.get(delegate.priority);
        if (set == null) {
            set = new HashSet<>();
            delegates.put(delegate.priority, set);
        }
        set.add(delegate);
        size++;
    }


    public boolean remove(EventHandlerDelegate delegate) {
        Set<EventHandlerDelegate> set = delegates.get(delegate.priority);
        if (set == null) {
            return false;
        }
        set.remove(delegate);
        if (set.size() == 0) {
            delegates.remove(delegate.priority);
        }
        size--;
        return true;
    }

    public void invoke(Event event, EventRegister register, EventPriority priority) {
        Set<EventHandlerDelegate> set = delegates.get(priority);
        if (set != null) {
            register._invokeDelegates(event, set);
        }
    }

    public int size() {
        return size;
    }
}
