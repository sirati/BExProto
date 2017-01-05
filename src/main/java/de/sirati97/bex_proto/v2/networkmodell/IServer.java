package de.sirati97.bex_proto.v2.networkmodell;

import de.sirati97.bex_proto.v2.artifcon.ArtifConnectionService;

import java.io.IOException;

/**
 * Created by sirati97 on 04.01.2017 for BexProto.
 */
public interface IServer<Connection extends ArtifConnectionService> extends IConnection<Connection> {
    void startListening() throws IOException;

    void stopListening();

    boolean isListening();
}
