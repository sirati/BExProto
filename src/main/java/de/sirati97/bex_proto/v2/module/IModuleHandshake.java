package de.sirati97.bex_proto.v2.module;

import de.sirati97.bex_proto.v2.module.internal.IHandshakeCallback;

/**
 * Created by sirati97 on 13.04.2016.
 */
public interface IModuleHandshake extends IModule {
    void onHandshake(ModularArtifConnection connection, IHandshakeCallback callback) throws Throwable;
    /**Only for preparing. DO NOT SEND PACKETS HERE*/
    void onHandshakeServerSide(ModularArtifConnection connection, IHandshakeCallback callback) throws Throwable;
    /**Called when handshake has finished. Also close or finalize all unneeded objects
     *@return Whether this packet has completed it handshake part*/
    boolean completeHandshake(ModularArtifConnection connection) throws Throwable;
    boolean hasHighHandshakePriority();
}
