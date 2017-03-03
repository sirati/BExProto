package de.sirati97.bex_proto.test.events;

import de.sirati97.bex_proto.events.EventHandler;
import de.sirati97.bex_proto.events.EventPriority;
import de.sirati97.bex_proto.events.EventRegister;
import de.sirati97.bex_proto.events.GenericEventHandler;
import de.sirati97.bex_proto.events.IEventDelegateListener;
import de.sirati97.bex_proto.events.Listener;
import de.sirati97.bex_proto.util.logging.ILogger;
import de.sirati97.bex_proto.util.logging.SysOutLogger;
import org.junit.Assert;
import org.junit.Test;

import java.lang.annotation.Annotation;

/**
 * Created by sirati97 on 29.04.2016.
 */
public class DelegateEventSystemTest implements Listener {
    private boolean received;


    public static void main(String... args) throws Throwable {
        DelegateEventSystemTest test = new DelegateEventSystemTest();
        test.start();
        Thread.sleep(200);
        test = new DelegateEventSystemTest();
        test.start();
        Thread.sleep(1000);
    }

    @Test
    public void start() {
        ILogger logger = new SysOutLogger();
        EventRegister register = new EventRegister(logger, true);
        register.registerEventListener(this);
        register.registerEventDelegateListener(new IEventDelegateListener<TestEvent>() {
            @Override
            public EventHandler getEventHandler() {
                return new EventHandler() {
                    @Override
                    public Class<? extends Annotation> annotationType() {
                        return (Class<? extends Annotation>) getClass();
                    }

                    @Override
                    public EventPriority priority() {
                        return EventPriority.Middle;
                    }

                    @Override
                    public boolean ignoreCancelled() {
                        return false;
                    }
                };
            }

            @Override
            public GenericEventHandler getGenericEventHandler() {
                return null;
            }

            @Override
            public Class<TestEvent> getEventClass() {
                return TestEvent.class;
            }

            @Override
            public void onEvent(TestEvent event) {
                received = true;
            }

            @Override
            public String getName() {
                return "TestUnitDelegate";
            }

            @Override
            public boolean autoUnregister() {
                return false;
            }
        });
        System.gc();
        register.invokeEvent(new TestEvent());
        register.invokeEvent(new TestEvent());
        register.invokeEvent(new TestEvent());
        Assert.assertTrue("Delegate was not called", received);
    }


    @EventHandler(priority = EventPriority.First)
    public void onTestEvent_First(TestEvent event) {
    }
}
