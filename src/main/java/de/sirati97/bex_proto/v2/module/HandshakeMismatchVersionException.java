package de.sirati97.bex_proto.v2.module;

import de.sirati97.bex_proto.datahandler.BExStatic;

/**
 * Created by sirati97 on 15.04.2016.
 */
public class HandshakeMismatchVersionException extends HandshakeException {

    public HandshakeMismatchVersionException(int remoteVersion) {
        this("BExProto", remoteVersion, BExStatic.VERSION_INT_MIN);
    }
    public HandshakeMismatchVersionException(String protocolName, int remoteVersion, int minVersion) {
        super("Remote used outdated version (" + remoteVersion + ") of " + protocolName + ". Minimum allowed version is " + minVersion);
    }

}
