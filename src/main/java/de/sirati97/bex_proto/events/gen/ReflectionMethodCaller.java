package de.sirati97.bex_proto.events.gen;

import de.sirati97.bex_proto.events.Event;
import de.sirati97.bex_proto.events.Listener;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by sirati97 on 30.04.2016.
 */
public class ReflectionMethodCaller implements MethodCaller {
    public static final ReflectionMethodCaller INSTANCE = new ReflectionMethodCaller();

    @Override
    public void invoke(Method method, Listener listener, Event event) throws InvocationTargetException, IllegalAccessException {
        boolean not_accessible = !method.isAccessible();
        if (not_accessible) {
            method.setAccessible(true);
        }
        method.invoke(listener, event);
        if (not_accessible) {
            method.setAccessible(false);
        }
    }
}
