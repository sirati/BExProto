package de.sirati97.bex_proto.events;

import de.sirati97.bex_proto.util.exception.NotImplementedException;

import java.lang.annotation.Annotation;

/**
 * Created by sirati97 on 25.04.2017 for BexProto.
 */
public abstract class EventDelegate<E extends Event> implements IEventDelegateListener<E>, EventHandler, GenericEventHandler {
    private final Class<E> eventClass;
    private Class[] generics;
    private EventPriority priority = EventPriority.Middle;
    private boolean ignoreCancelled = false;

    public EventDelegate(Class eventClass) {
        //noinspection unchecked
        this.eventClass = eventClass;
    }

    @Override
    public EventHandler getEventHandler() {
        return this;
    }

    @Override
    public GenericEventHandler getGenericEventHandler() {
        return generics==null?null:this;
    }

    @Override
    public Class<E> getEventClass() {
        return eventClass;
    }

    @Override
    public String getName() {
        return getClass().getSimpleName() + "#" + eventClass.getSimpleName();
    }

    @Override
    public EventPriority priority() {
        return priority;
    }

    public EventDelegate<E> priority(EventPriority value) {
        this.priority = value;
        return this;
    }

    @Override
    public boolean autoUnregister() {
        return true;
    }

    @Override
    public boolean ignoreCancelled() {
        return ignoreCancelled;
    }

    public EventDelegate<E> ignoreCancelled(boolean value) {
        this.ignoreCancelled = value;
        return this;
    }

    @Override
    public Class[] generics() {
        return generics;
    }

    public EventDelegate<E> generics(Class... value) {
        this.generics = value;
        return this;
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        throw new NotImplementedException("This class only represents the values of an event handler and is not an actual annotation.");
    }
}
