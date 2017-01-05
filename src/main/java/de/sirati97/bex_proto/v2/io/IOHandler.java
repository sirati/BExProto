package de.sirati97.bex_proto.v2.io;

import de.sirati97.bex_proto.v2.artifcon.ArtifConnectionService;

import java.io.IOException;

/**
 * Created by sirati97 on 15.03.2016.
 */
public interface IOHandler {
    void send(byte[] stream, boolean reliable) throws IOException;
    boolean isOpen();
    void close();
    void open() throws IOException;
    void setConnection(ArtifConnectionService connection);
    void updateConnectionName(String newName);
}
