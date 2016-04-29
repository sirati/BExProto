package de.sirati97.bex_proto.events;

/**
 * Created by sirati97 on 29.04.2016.
 */
public class EventDistributorImpl implements EventDistributor {
    @Override
    public void distribute(Event event, EventRegister register) {
        register.invokeDelegates(event, register.getDelegates(event.getClass()));
    }
}
