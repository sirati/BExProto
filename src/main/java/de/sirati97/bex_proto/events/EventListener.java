package de.sirati97.bex_proto.events;

/**
 * Created by sirati97 on 01.05.2016.
 */
public interface EventListener extends Listener {
    <T extends Event> void onEvent(T event, Class<? extends T> eventClass);
}
