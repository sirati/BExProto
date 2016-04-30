package de.sirati97.bex_proto.test.events;

import de.sirati97.bex_proto.events.EventHandler;
import de.sirati97.bex_proto.events.Listener;
import de.sirati97.bex_proto.events.gen.ClassBuilder;
import de.sirati97.bex_proto.events.gen.MethodCaller;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by sirati97 on 30.04.2016.
 */
public class MethodCallerBenchmark implements Listener {

    @Test
    public void start() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, IOException {
        ClassBuilder builder = ClassBuilder.INSTANCE;
        System.out.println("Runs micro benchmark. Event handler called 10,000,000,000 times.");
        long timestampBuild = System.nanoTime();
        //compiles caller
        java.lang.reflect.Method method = MethodCallerBenchmark.class.getDeclaredMethod("onTestEvent", TestEvent.class);
        MethodCaller caller = builder.getEventCallerBlocking(method);
        timestampBuild = (System.nanoTime()-timestampBuild)/1000000;

        long timestamp = System.nanoTime();
        for (long i=0;i<10_000_000_000L;i++) {
            this.onTestEvent(null);
        }
        System.out.println("Direct call:            " + ((System.nanoTime()-timestamp)/1000000)+"ms");


        //tests
        timestamp = System.nanoTime();
        for (long i=0;i<10_000_000_000L;i++) {
            caller.invoke(method, this, null);
        }
        timestamp = (System.nanoTime()-timestamp)/1000000;
        System.out.println("Runtime-generated call: " + (timestamp+timestampBuild)+"ms ("+timestampBuild+"/"+timestamp+")");

        ClassBuilder.generateClasses = false;

        timestamp = System.nanoTime();
        caller = builder.getEventCallerBlocking(method);
        //tests
        for (long i=0;i<10_000_000_000L;i++) {
            caller.invoke(method, this, null);
        }
        System.out.println("Reflection call:        " + ((System.nanoTime()-timestamp)/1000000)+"ms");
    }

    @EventHandler
    public void onTestEvent(TestEvent event) {
    }
}
