package de.sirati97.bex_proto.events;

/**
 * Created by sirati97 on 09.05.2016.
 */
public class MalformedListenerClassException extends RuntimeException {

    /**
     * Constructs an IllegalStateException with the specified detail
     * message.  A detail message is a String that describes this particular
     * exception.
     *
     * @param s the String that contains a detailed message
     */
    public MalformedListenerClassException(String s) {
        super(s);
    }
}
