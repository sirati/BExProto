package de.sirati97.bex_proto.threading;

/**
 * Created by sirati97 on 09.05.2016.
 */
public interface AsyncTask {
    void stop();
    boolean isRunning();
    String getName();
    void setName(String name);
    Thread getThread();
}
