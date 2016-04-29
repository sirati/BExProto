package de.sirati97.bex_proto.test.events;

import de.sirati97.bex_proto.events.Cancelable;
import de.sirati97.bex_proto.events.Event;
import de.sirati97.bex_proto.events.EventDistributor;
import de.sirati97.bex_proto.events.EventDistributorImpl;

/**
 * Created by sirati97 on 29.04.2016.
 */
public class TestCancelableEvent implements Event, Cancelable {
    private static final EventDistributor DISTRIBUTOR = new EventDistributorImpl();
    private boolean cancelled = false;

    public static EventDistributor getEventDistributor() {
        return DISTRIBUTOR;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
