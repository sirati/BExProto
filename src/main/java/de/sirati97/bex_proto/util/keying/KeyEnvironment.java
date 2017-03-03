package de.sirati97.bex_proto.util.keying;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by sirati97 on 03.03.2017 for BexProto.
 */
public class KeyEnvironment {
    private final AtomicInteger counter = new AtomicInteger(0);

    public synchronized int getNext() {
        return counter.getAndIncrement();
    }
}
