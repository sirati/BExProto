package de.sirati97.bex_proto.events;

/**
 * Created by sirati97 on 09.05.2016.
 */
public abstract class EventClassHelperBase implements EventClassHelper {

    @Override
    public String getEventDistributorMissingMessage(Class<? extends Event> eventClass) {
        return "Event " + eventClass.getName() + " need to has 'public static EventDistributor getEventDistributor();'";
    }

    @Override
    public String getGenericSuperclassesMissingMessage(Class<? extends Event> eventClass) {
        return "Event " + eventClass.getName() + " need to has 'public static Class[] getGenericsSuperclasses();'";
    }
}
