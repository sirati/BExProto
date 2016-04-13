package de.sirati97.bex_proto.util.logging;

/**
 * Created by sirati97 on 13.04.2016.
 */
public interface ILogger {
    void log(String text);
    void info(String text);
    void warn(String text);
    void error(String text);
    ILogger getLogger(String prefix);
    String getPrefix();
    void changePrefix(String prefix);

}
