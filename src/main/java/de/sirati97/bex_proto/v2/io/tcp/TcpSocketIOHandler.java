package de.sirati97.bex_proto.v2.io.tcp;

import de.sirati97.bex_proto.threading.AsyncHelper.AsyncTask;
import de.sirati97.bex_proto.v2.artifcon.ArtifConnection;
import de.sirati97.bex_proto.v2.io.IOHandler;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

/**
 * Created by sirati97 on 17.04.2016.
 */
public class TcpSocketIOHandler implements IOHandler {
    private ArtifConnection connection;
    private boolean open = false;
    private final Socket socket;
    private AsyncTask listenerTask;

    public TcpSocketIOHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public synchronized void send(byte[] stream, boolean reliable) throws IOException {
        if (!isOpen()) {
            throw new IOException("Pipe is not open");
        }
        socket.getOutputStream().write(stream);
    }

    @Override
    public synchronized boolean isOpen() {
        return open && socket.isConnected();
    }

    @Override
    public synchronized void close() {
        try {
            open = false;
            if (listenerTask != null && listenerTask.isRunning()) {
                listenerTask.stop();
            }
            if (socket.isConnected()) {
                socket.close();
            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public synchronized void open() throws IOException {
        if (isOpen()) throw new IOException("Socket is already open");
        if (connection == null) throw new IllegalStateException("Connection not set");
        if (!socket.isConnected()) throw  new IOException("Socket is closed");
        open = true;
        listenerTask = connection.getAsyncHelper().runAsync(new Runnable() {
            @Override
            public void run() {
                try {
                    InputStream in = socket.getInputStream();
                    while (open && !Thread.interrupted()) {
                        try {
                            int available;
                            if ((available=in.available()) > 0) {
                                byte[] read = new byte[available];
                                in.read(read);
                                connection.read(read);
                            } else {
                                Thread.sleep(0, 1);
                            }
                        } catch (IOException e) {
                            connection.onIOException(e);
                        } catch (InterruptedException e) {
                            break;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (open) {
                        connection.disconnect();
                    }
                }
            }
        },connection.getConnectionName() + " Listener Thread");
    }

    @Override
    public void setConnection(ArtifConnection connection) {
        this.connection = connection;
    }

    @Override
    public void updateConnectionName(String newName) {
        if (listenerTask!=null) {
            listenerTask.setName(newName + " Listener Thread");
        }
    }
}
