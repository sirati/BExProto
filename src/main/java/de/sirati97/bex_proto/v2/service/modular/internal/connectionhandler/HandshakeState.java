package de.sirati97.bex_proto.v2.service.modular.internal.connectionhandler;

/**
 * Created by sirati97 on 16.04.2016.
 */
public enum HandshakeState {
    NoHandshake(false),State1(true),State2(true),State3(true),Done(false);

    private final boolean inHandshake;

    HandshakeState(boolean inHandshake) {
        this.inHandshake = inHandshake;
    }

    public boolean isInHandshake() {
        return inHandshake;
    }
}
