package de.sirati97.bex_proto.v2.modular.internal;

/**
 * Created by sirati97 on 16.04.2016.
 */
public enum YieldCause {
    /**Should be used when a packet was received*/
    PacketReceived,
    /**Should be used when a algorithm takes more than 1000 milliseconds*/
    KeepAlive
}
