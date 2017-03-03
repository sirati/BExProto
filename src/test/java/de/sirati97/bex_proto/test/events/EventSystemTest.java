package de.sirati97.bex_proto.test.events;

import de.sirati97.bex_proto.events.EventHandler;
import de.sirati97.bex_proto.events.EventPriority;
import de.sirati97.bex_proto.events.EventRegister;
import de.sirati97.bex_proto.events.IEventRegister;
import de.sirati97.bex_proto.events.Listener;
import de.sirati97.bex_proto.util.logging.ILogger;
import de.sirati97.bex_proto.util.logging.SysOutLogger;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by sirati97 on 29.04.2016.
 */
public class EventSystemTest implements Listener {
    private int state = -1;


    public static void main(String... args) throws Throwable {
        EventSystemTest test = new EventSystemTest();
        test.start();
        Thread.sleep(200);
        test = new EventSystemTest();
        test.start();
        Thread.sleep(1000);
    }

    @Test
    public void start() {
        ILogger logger = new SysOutLogger();
        EventRegister register = new EventRegister(logger, true);
        register.registerEventListener(this);

        testRegister(register);

        EventRegister register2 = new EventRegister(logger, true);
        register2.addParent(register);

        boolean caught = false;
        try {
            register.addParent(register2);
        } catch (Throwable t) {
            caught = true;
        }
        Assert.assertTrue("Should not be able to add child as parent", caught);

        testRegister(register2);

    }

    private void testRegister(IEventRegister register) {
        boolean caught = false;
        try {
            register.invokeEvent(new FailEvent());
        } catch (Throwable t) {
            caught = true;
        }
        Assert.assertTrue("Should not be able to invoke event without distributor", caught);


        state = 0;
        register.invokeEvent(new TestEvent());
        Assert.assertTrue("Event was not invoked properly", state == 5);
        state = 0;


        TestCancelableEvent event = new TestCancelableEvent();
        register.invokeEvent(event);
        Assert.assertTrue("Event was not invoked properly", state == 5);
        Assert.assertTrue("Event should be cancelled", event.isCancelled());
    }

    @EventHandler(priority = EventPriority.First)
    public void onTestEvent_First(TestEvent event) {
        Assert.assertTrue("Event called in wrong order! state=" + state, state == 1);
        state = 2;
    }


    @EventHandler(priority = EventPriority.Prepare)
    public void onTestEvent_Prepare(TestEvent event) {
        Assert.assertTrue("Event called in wrong order! state=" + state, state == 0);
        state = 1;
    }

    @EventHandler(priority = EventPriority.Middle)
    public void onTestEvent_Middle(TestEvent event) {
        Assert.assertTrue("Event called in wrong order! state=" + state, state == 2);
        state = 3;
    }

    @EventHandler(priority = EventPriority.Last)
    public void onTestEvent_Last(TestEvent event) {
        Assert.assertTrue("Event called in wrong order! state=" + state, state == 3);
        state = 4;
    }

    @EventHandler(priority = EventPriority.Monitor)
    public void onTestEvent_Monitor(TestEvent event) {
        Assert.assertTrue("Event called in wrong order! state=" + state, state == 4);
        state = 5;
    }

    @EventHandler(priority = EventPriority.Prepare)
    public void onTestCancelableEvent_Prepare(TestCancelableEvent event) {
        Assert.assertTrue("Event called in wrong order! state=" + state, state == 0);
        state = 1;
    }

    @EventHandler(priority = EventPriority.First)
    public void onTestCancelableEvent_First(TestCancelableEvent event) {
        Assert.assertTrue("Event called in wrong order! state=" + state, state == 1);
        event.setCancelled(true);
        state = 2;
    }

    @EventHandler(priority = EventPriority.Middle, ignoreCancelled = true)
    public void onTestCancelableEvent_Middle(TestCancelableEvent event) {
        Assert.assertTrue("Event called in wrong order! state=" + state, state == 2);
        state = 5;
    }


    @EventHandler(priority = EventPriority.Monitor)
    public void onTestCancelableEvent_Monitor(TestCancelableEvent event) {
        Assert.fail("Event is cancelled. should not be called!");
    }



}
