package de.sirati97.bex_proto.test.events;

import de.sirati97.bex_proto.events.Event;
import de.sirati97.bex_proto.events.EventDistributor;
import de.sirati97.bex_proto.events.EventDistributorImpl;

/**
 * Created by sirati97 on 29.04.2016.
 */
public class TestEvent implements Event {
    private static final EventDistributor DISTRIBUTOR = new EventDistributorImpl();

    public static EventDistributor getEventDistributor() {
        return DISTRIBUTOR;
    }
}
