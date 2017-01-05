package de.sirati97.bex_proto.v2.modular.internal;

/**
 * Created by sirati97 on 13.04.2016.
 */
public interface ICallback {
    //*Should be called when your modular finished*/
    void callback();
    //*Should be called when one of your methods throw an exception*/
    void error(Throwable cause);
    //*Should be called to once all 1s and every time you receive an answer to suppress a timeout exception
    //
    //Will only have effect if a yield would make sense*/
    void yield(YieldCause cause);
    boolean hasTimeoutOccurred();
}
