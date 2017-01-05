package de.sirati97.bex_proto.v2.io.tcp;

import de.sirati97.bex_proto.builder.ITcpAddress;
import de.sirati97.bex_proto.threading.AsyncTask;
import de.sirati97.bex_proto.util.logging.ILogger;
import de.sirati97.bex_proto.v2.IConnectionServiceFactory;
import de.sirati97.bex_proto.v2.artifcon.ArtifConnectionService;
import de.sirati97.bex_proto.v2.networkmodell.INetworkProtocol;
import de.sirati97.bex_proto.v2.networkmodell.INetworkStackImplementation;
import de.sirati97.bex_proto.v2.networkmodell.ServerBase;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;

import static de.sirati97.bex_proto.v2.networkmodell.CommonNetworkProtocols.TCP;
import static de.sirati97.bex_proto.v2.networkmodell.CommonNetworkStackImplementation.BlockingIO;

/**
 * Created by sirati97 on 17.04.2016.
 */
public class TcpBIOServer<Connection extends ArtifConnectionService> extends ServerBase<Connection> {
    private final ServerSocket serverSocket = new ServerSocket();
    private AsyncTask task;
    private boolean listening = false;
    private final ITcpAddress address;

    public TcpBIOServer(IConnectionServiceFactory<Connection> factory, ITcpAddress address) throws IOException {
        super(factory);
        this.address = address;
    }

    @Override
    public synchronized void startListening() throws IOException {
        if (task != null) {
            throw new IllegalStateException("Server was already started");
        }
        if (!listening) {
            if (!serverSocket.isBound()) {
                serverSocket.bind(address.getServer());
            }
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
                                    Connection connection = getFactory().createServer(new TcpSocketBIOHandler(socket));
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
        return getFactory().getLogger().getLogger("TcpBIOServer{ip="+serverSocket.getInetAddress().getHostAddress()+", port="+serverSocket.getLocalPort()+"}");
    }

    @Override
    public INetworkProtocol getUnderlyingProtocol() {
        return TCP;
    }

    @Override
    public INetworkStackImplementation getNetworkStackImplementation() {
        return BlockingIO;
    }
}
