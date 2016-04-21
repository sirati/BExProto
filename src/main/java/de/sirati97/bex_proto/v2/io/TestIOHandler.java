package de.sirati97.bex_proto.v2.io;

import de.sirati97.bex_proto.v2.artifcon.ArtifConnection;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

/**
 * Created by sirati97 on 13.04.2016.
 */
public class TestIOHandler implements IOHandler {
    private ArtifConnection connection;
    public TestIOHandler receiver;
    private boolean open = false;
    private boolean closed = false;
    private final Object lock = new Object();

    @Override
    public synchronized void send(byte[] stream, boolean reliable) throws IOException {
        if (!open) {
            throw new IOException("Pipe is not open");
        }
        System.out.println(bytesToString(stream));
        receiver.read(stream);
    }

    public void read(byte[] stream) throws IOException {
        synchronized (lock) {
            if (!open && !closed) {
                try {
                    lock.wait(); //will wait until it is opened
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        if (closed) {
            throw new IOException("Pipe is not open");
        }
        connection.read(stream);
    }

    @Override
    public boolean isOpen() {
        return open;
    }

    @Override
    public synchronized void close() {
        open = false;
        closed = true;
    }

    @Override
    public synchronized void open() throws IOException {
        if (receiver == null) {
            throw new IOException("Receiver not set");
        }
        synchronized (lock) {
            lock.notifyAll();
        }
        open = true;
    }

    @Override
    public synchronized void setConnection(ArtifConnection connection) {
        this.connection = connection;
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
