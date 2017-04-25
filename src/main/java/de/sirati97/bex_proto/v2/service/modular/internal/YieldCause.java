package de.sirati97.bex_proto.v2.service.modular.internal;

/**
 * Created by sirati97 on 16.04.2016.
 */
public enum YieldCause {
    /**Should be used when a packet was received*/
    PacketReceived(true, false),
    /**Should be used when a algorithm takes more than 1000 milliseconds*/
    KeepAliveLong(false, true),
    /**Should be used when a algorithm takes more than 100 milliseconds*/
    KeepAlive(false, false),
    /**For internal use*/
    KeepAliveReceived(true, false),
    /**For internal use*/
    KeepAliveLongReceived(true, true);

    private final boolean received;
    private final boolean longYield;

    YieldCause(boolean received, boolean longYield) {
        this.received = received;
        this.longYield = longYield;
    }

    public boolean isReceived() {
        return received;
    }

    public boolean isLongYield() {
        return longYield;
    }
}
