package de.sirati97.bex_proto.v2.service.modular;

/**
 * Created by sirati97 on 15.04.2016.
 */
public interface IConnectResult {
    void onConnected(ModularService connection);
    void onException(ModularService connection, Exception e);
}
