package de.sirati97.bex_proto.v2.io.tcp;

import de.sirati97.bex_proto.datahandler.InetAddressPort;
import de.sirati97.bex_proto.threading.AsyncHelper.AsyncTask;
import de.sirati97.bex_proto.util.logging.ILogger;
import de.sirati97.bex_proto.v2.IConnectionFactory;
import de.sirati97.bex_proto.v2.ServerBase;
import de.sirati97.bex_proto.v2.artifcon.ArtifConnection;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;

/**
 * Created by sirati97 on 17.04.2016.
 */
public class TcpServer<Connection extends ArtifConnection> extends ServerBase<Connection> {
    private final ServerSocket serverSocket;
    private AsyncTask task;
    private boolean listening = false;
    private final int port;

    protected TcpServer(IConnectionFactory<Connection> factory, ServerSocket serverSocket, int port) {
        super(factory);
        this.serverSocket = serverSocket;
        this.port = port;
    }

    public TcpServer(IConnectionFactory<Connection> factory, int port) throws IOException {
        this(factory, new ServerSocket(port), port);
    }

    public TcpServer(IConnectionFactory<Connection> factory, InetAddress address, int port) throws IOException {
        this(factory, new ServerSocket(port, 0, address), port);
    }

    public TcpServer(IConnectionFactory<Connection> factory, InetAddressPort inetAddressPort) throws IOException {
        this(factory, inetAddressPort.getInetAddress(), inetAddressPort.getPort());
    }

    public TcpServer(IConnectionFactory<Connection> factory, InetSocketAddress socketAddress) throws IOException {
        this(factory, socketAddress.getAddress(), socketAddress.getPort());
    }

    @Override
    public synchronized void startListening() {
        if (task != null) {
            throw new IllegalStateException("Server was already started");
        }
        if (!listening) {
            listening = true;
            task = getAsyncHelper().runAsync(new Runnable() {
                @Override
                public void run() {
                    while(listening && !Thread.interrupted()) {

                        Socket socket = null;
                        try {
                            if (serverSocket.isClosed()) {
                                getLogger().warn("The socket was closed!");
                            } else if ((socket = serverSocket.accept()) != null) {
                                try {
                                    Connection connection = getFactory().createServer(new TcpSocketIOHandler(socket));
                                    registerConnection(connection);
                                    connection.expectConnection();
                                } catch (Throwable e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch (IOException e) {
                            if (!(e instanceof java.net.SocketException) && !listening) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }, "Server Listener Thread");
        }
    }

    @Override
    public void stopListening() {
        if (listening) {
            listening = false;
            task.stop();
        }
        try {
            serverSocket.setSoTimeout(0);
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {
        stopListening();
        for (Connection connection:new HashSet<>(getConnections())) {
            connection.disconnect();
        }
    }

    @Override
    public synchronized boolean isListening() {
        return serverSocket.isBound() && !serverSocket.isClosed() && task != null && task.isRunning();
    }

    @Override
    protected ILogger createLogger() {
        return getFactory().getLogger().getLogger("TcpServer{ip"+serverSocket.getInetAddress().getHostAddress()+", port="+serverSocket.getLocalPort()+"}");
    }
}
