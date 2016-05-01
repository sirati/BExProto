package de.sirati97.bex_proto.v2.io;

import de.sirati97.bex_proto.v2.artifcon.ArtifConnection;

import java.io.IOException;

/**
 * Created by sirati97 on 01.05.2016.
 */
public abstract class IOHandlerBase implements IOHandler {
    private ArtifConnection connection;
    private volatile boolean open = false;


    @Override
    public synchronized final void send(byte[] stream, boolean reliable) throws IOException {
        if (!isOpen()) {
            throw new IOException("Pipe is not open");
        }
        sendInternal(stream, reliable);
    }

    protected abstract void sendInternal(byte[] stream, boolean reliable) throws IOException;

    @Override
    public synchronized final boolean isOpen() {
        return open && isIOOpen();
    }

    protected abstract boolean isIOOpen();
    protected abstract void closeIO() throws IOException;
    protected abstract void onClose();

    @Override
    public synchronized final void close() {
        try {
            open = false;
            onClose();
            if (isIOOpen()) {
                closeIO();
            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public synchronized final void open() throws IOException {
        if (isOpen()) throw new IOException("Socket is already open");
        if (connection == null) throw new IllegalStateException("Connection not set");
        if (!isIOOpen()) throw  new IOException("Socket is closed");
        open = true;

        startReading();
    }

    protected abstract void startReading() throws IOException;

    protected final boolean getOpenFlag() {
        return open;
    }

    @Override
    public void setConnection(ArtifConnection connection) {
        this.connection = connection;
    }

    public ArtifConnection getConnection() {
        return connection;
    }

    @Override
    public void updateConnectionName(String newName) {

    }
}
