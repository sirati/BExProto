package de.sirati97.bex_proto.v2.module;

import de.sirati97.bex_proto.v2.module.internal.IHandshakeCallback;

/**
 * Created by sirati97 on 13.04.2016.
 */
public interface IModuleHandshake extends IModule {
    void onHandshake(ModularArtifConnection connection, IHandshakeCallback callback);
    boolean hasHighHandshakePriority();
}
