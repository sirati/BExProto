package de.sirati97.bex_proto.util.logging;

/**
 * Created by sirati97 on 13.04.2016.
 */
public class SysOutLogger implements ILogger {
    private String prefix;

    public SysOutLogger(String prefix) {
        this.prefix = prefix;
    }
    public SysOutLogger() {
        this(null);
    }

    @Override
    public void log(String text) {
        System.out.println(genText(text));
    }

    @Override
    public void info(String text) {
        System.out.println(genText(text));
    }

    @Override
    public void warn(String text) {
        System.err.println(genText(text));
    }

    @Override
    public void error(String text) {
        System.err.println(genText(text));
    }

    private String genText(String text) {
        return (prefix==null?"":"["+prefix+"] ")+text;
    }

    @Override
    public ILogger getLogger(String prefix) {
        return new SysOutLogger(prefix);
    }

    @Override
    public String getPrefix() {
        return prefix;
    }

    @Override
    public void changePrefix(String prefix) {
        this.prefix = prefix;
    }
}
