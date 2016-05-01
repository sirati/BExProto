package de.sirati97.bex_proto.v2.io.tcp;

import de.sirati97.bex_proto.datahandler.InetAddressPort;
import de.sirati97.bex_proto.util.logging.ILogger;
import de.sirati97.bex_proto.v2.IConnectionFactory;
import de.sirati97.bex_proto.v2.ServerBase;
import de.sirati97.bex_proto.v2.artifcon.ArtifConnection;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.channels.ShutdownChannelGroupException;
import java.util.HashSet;

/**
 * Created by sirati97 on 17.04.2016.
 */
public class TcpAIOServer<Connection extends ArtifConnection> extends ServerBase<Connection> {
    private final AsynchronousServerSocketChannel serverSocket;
    private final InetSocketAddress socketAddress;
    private boolean listening = false;

    protected TcpAIOServer(IConnectionFactory<Connection> factory, AsynchronousServerSocketChannel serverSocket, InetSocketAddress socketAddress) {
        super(factory);
        this.serverSocket = serverSocket;
        this.socketAddress = socketAddress;
    }

    public TcpAIOServer(IConnectionFactory<Connection> factory, int port) throws IOException {
        this(factory, new InetSocketAddress((InetAddress)null, port));
    }

    public TcpAIOServer(IConnectionFactory<Connection> factory, InetAddress address, int port) throws IOException {
        this(factory, new InetSocketAddress(address, port));
    }

    public TcpAIOServer(IConnectionFactory<Connection> factory, InetAddressPort inetAddressPort) throws IOException {
        this(factory, inetAddressPort.toInetSocketAddress());
    }

    public TcpAIOServer(IConnectionFactory<Connection> factory, InetSocketAddress socketAddress) throws IOException {
        this(factory, AsynchronousServerSocketChannel.open(), socketAddress);
    }

    @Override
    public synchronized void startListening() throws IOException{
        if (listening) {
            throw new IllegalStateException("Server was already started");
        }
        serverSocket.bind(socketAddress);
        listen();
    }

    private void listen() {
        serverSocket.accept(null, new CompletionHandler<AsynchronousSocketChannel, Object>() {
            @Override
            public void completed(AsynchronousSocketChannel socket, Object attachment) {
                try {
                    Connection connection = getFactory().createServer(new TcpSocketAIOHandler(socket));
                    registerConnection(connection);
                    connection.expectConnection();
                } catch (Throwable e) {
                    if (e instanceof ShutdownChannelGroupException) {
                        return;
                    }
                    e.printStackTrace();
                }
            }

            @Override
            public void failed(Throwable e, Object attachment) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void stopListening() {
        if (listening) {
            listening = false;
        }
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {
        for (Connection connection:new HashSet<>(getConnections())) {
            connection.disconnect();
        }
        stopListening();
    }

    @Override
    public synchronized boolean isListening() {
        return listening && serverSocket.isOpen();
    }

    @Override
    protected ILogger createLogger() {
        return getFactory().getLogger().getLogger("TcpAIOServer{ip"+socketAddress.getAddress().getHostAddress()+", port="+socketAddress.getPort()+"}");
    }
}
