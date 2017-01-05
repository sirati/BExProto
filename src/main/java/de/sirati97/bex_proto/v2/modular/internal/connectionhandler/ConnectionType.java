package de.sirati97.bex_proto.v2.modular.internal.connectionhandler;

/**
 * Created by sirati97 on 24.04.2016.
 */
public enum ConnectionType {
    Server(1),Client(0),Peer(2);

    private final int other;

    ConnectionType(int other) {
        this.other = other;
    }

    public ConnectionType getOtherSide() {
        return values()[other];
    }
}
