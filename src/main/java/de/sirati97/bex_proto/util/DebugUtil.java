package de.sirati97.bex_proto.util;

/**
 * Created by sirati97 on 03.03.2017 for BexProto.
 */
public class DebugUtil {

    public static void printStacktrace() {
        StackTraceElement[] elements = Thread.currentThread().getStackTrace();
        for (int i = 2; i < elements.length; i++) {
            System.out.println("\tat " + elements[i]);
        }
    }
}
