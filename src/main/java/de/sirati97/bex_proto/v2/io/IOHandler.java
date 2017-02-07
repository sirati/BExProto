package de.sirati97.bex_proto.v2.io;

import de.sirati97.bex_proto.util.bytebuffer.ByteBuffer;
import de.sirati97.bex_proto.v2.service.basic.BasicService;

import java.io.IOException;

/**
 * Created by sirati97 on 15.03.2016.
 */
public interface IOHandler {
    void send(ByteBuffer stream, boolean reliable) throws IOException;
    boolean isOpen();
    void close();
    void open() throws IOException;
    void setConnection(BasicService connection);
    void updateConnectionName(String newName);
}
