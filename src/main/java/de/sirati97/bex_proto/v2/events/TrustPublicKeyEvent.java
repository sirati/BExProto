package de.sirati97.bex_proto.v2.events;

import de.sirati97.bex_proto.events.Event;
import de.sirati97.bex_proto.events.EventDistributor;
import de.sirati97.bex_proto.events.EventDistributorImpl;

import java.security.PublicKey;

/**
 * Created by sirati97 on 29.04.2016.
 */
public class TrustPublicKeyEvent implements Event {
    private static final EventDistributor DISTRIBUTOR = new EventDistributorImpl();
    public static EventDistributor getEventDistributor() {
        return DISTRIBUTOR;
    }
    private final PublicKey publicKey;
    private boolean trusted;

    public TrustPublicKeyEvent(PublicKey publicKey, boolean trusted) {
        this.publicKey = publicKey;
        this.trusted = trusted;
    }

    public boolean isTrusted() {
        return trusted;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void setTrusted(boolean trusted) {
        this.trusted = trusted;
    }
}


