package de.sirati97.bex_proto.util.logging;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by sirati97 on 24.04.2017 for EpicPlugins.
 */
public class JavaLogger implements ILogger {
    private Logger internal;

    public JavaLogger(Logger javaLogger) {
        this.internal = javaLogger;
    }

    @Override
    public void log(String s) {
        info(s);
    }

    @Override
    public void info(String s) {
        internal.info(s);
    }

    @Override
    public void warn(String s) {
        internal.warning(s);
    }

    @Override
    public void error(String s) {
        internal.severe(s);
    }

    @Override
    public void error(String s, Throwable throwable) {
        internal.log(Level.SEVERE, s, throwable);
    }

    @Override
    public ILogger getLogger(String s) {
        return new JavaLogger(Logger.getLogger(s));
    }

    @Override
    public String getPrefix() {
        return internal.getName();
    }

    @Override
    public void changePrefix(String s) {
        internal = Logger.getLogger(s);
    }
}
