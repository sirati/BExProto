package de.sirati97.bex_proto.events;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by sirati97 on 03.03.2017 for BexProto.
 */
public class EventHandlerDelegateListenerDelegate implements IEventHandlerDelegate {
    private final WeakReference<IEventDelegateListener> instance;
    private final Class<? extends Event> eventClass;
    private final EventPriority priority;

    public EventHandlerDelegateListenerDelegate(WeakReference<IEventDelegateListener> instance) {
        this.instance = instance;
        IEventDelegateListener<?> d = instance.get();

        if (d == null) {
            throw new IllegalArgumentException("Delegate-listener can not be disposed while event registration");
        }

        eventClass = d.getEventClass();
        priority = d.getEventHandler().priority();

    }

    @Override
    public void invoke(Event event) throws InvocationTargetException, IllegalAccessException {
        IEventDelegateListener delegate = instance.get();
        if (delegate == null || !EventHandlerListenerDelegate.checkRaisedEvent(event, delegate.getEventHandler().ignoreCancelled(), delegate.getGenericEventHandler(), EventHelperUtil.DELEGATE_LISTENER_NAME_RECEIVER, delegate)) {
            return;
        }
        //noinspection unchecked
        delegate.onEvent(event);
    }

    @Override
    public Class<? extends Event> getEventClass() {
        return eventClass;
    }

    @Override
    public EventPriority getPriority() {
        return priority;
    }
}
