package de.sirati97.bex_proto.v2.modular;

import de.sirati97.bex_proto.v2.modular.internal.ICallback;

/**
 * Created by sirati97 on 13.04.2016.
 */
public interface IModuleHandshake extends IModule {
    void onHandshake(ModularArtifConnectionService connection, ICallback callback) throws Throwable;
    /**Only for preparing. DO NOT SEND PACKETS HERE*/
    void onHandshakeServerSide(ModularArtifConnectionService connection, ICallback callback) throws Throwable;
    /**Called when handshake has finished. Also close or finalize all unneeded objects
     *@return Whether this packet has completed it handshake part*/
    boolean completeHandshake(ModularArtifConnectionService connection) throws Throwable;
    boolean hasHighHandshakePriority();
}
