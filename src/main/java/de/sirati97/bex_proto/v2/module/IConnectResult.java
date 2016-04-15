package de.sirati97.bex_proto.v2.module;

/**
 * Created by sirati97 on 15.04.2016.
 */
public interface IConnectResult {
    void onConnected(ModularArtifConnection connection);
    void onException(ModularArtifConnection connection, Exception e);
}
