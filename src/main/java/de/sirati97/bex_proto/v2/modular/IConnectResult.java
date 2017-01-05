package de.sirati97.bex_proto.v2.modular;

/**
 * Created by sirati97 on 15.04.2016.
 */
public interface IConnectResult {
    void onConnected(ModularArtifConnectionService connection);
    void onException(ModularArtifConnectionService connection, Exception e);
}
