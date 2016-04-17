package de.sirati97.bex_proto.v2.io;

import de.sirati97.bex_proto.v2.artifcon.ArtifConnection;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

/**
 * Created by sirati97 on 13.04.2016.
 */
public class TestIOHandler implements IOHandler {
    public ArtifConnection receiver;
    private boolean open = false;

    @Override
    public synchronized void send(byte[] stream) throws IOException {
        if (!open) {
            throw new IOException("Pipe is not open");
        }
        System.out.println(bytesToString(stream));
        receiver.read(stream);
    }

    @Override
    public boolean isOpen() {
        return open;
    }

    @Override
    public synchronized void close() {
        open = false;
    }

    @Override
    public synchronized void open() throws IOException {
        if (receiver == null) {
            throw new IOException("Pipe is not open");
        }
        open = true;
    }

    @Override
    public synchronized void setConnection(ArtifConnection connection) {

    }

    @Override
    public void updateConnectionName(String newName) {

    }


    public static String bytesToString(byte[] stream) {
        StringBuilder sb = new StringBuilder();
        for (byte b:stream) {
            String num = String.valueOf((int)b & 0x000000FF);
            sb.append(StringUtils.leftPad(num, 4 , ' '));
        }
        return sb.toString();
    }


}
