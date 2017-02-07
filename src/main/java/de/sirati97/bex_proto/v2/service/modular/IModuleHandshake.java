package de.sirati97.bex_proto.v2.service.modular;

import de.sirati97.bex_proto.v2.service.modular.internal.ICallback;

/**
 * Created by sirati97 on 13.04.2016.
 */
public interface IModuleHandshake extends IModule {
    void onHandshake(ModularService connection, ICallback callback) throws Throwable;
    /**Only for preparing. DO NOT SEND PACKETS HERE*/
    void onHandshakeServerSide(ModularService connection, ICallback callback) throws Throwable;
    /**Called when handshake has finished. Also close or finalize all unneeded objects
     *@return Whether this packet has completed it handshake part*/
    boolean completeHandshake(ModularService connection) throws Throwable;
    boolean hasHighHandshakePriority();
}
