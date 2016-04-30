package de.sirati97.bex_proto.events.gen;

/**
 * Created by sirati97 on 30.04.2016.
 */
public interface GenerateTaskCallback {
    void done(MethodCaller methodCaller);
    void error(Throwable t);
}
