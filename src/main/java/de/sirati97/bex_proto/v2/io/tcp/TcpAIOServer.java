package de.sirati97.bex_proto.v2.io.tcp;

import de.sirati97.bex_proto.builder.ITcpAddress;
import de.sirati97.bex_proto.util.logging.ILogger;
import de.sirati97.bex_proto.v2.IServiceFactory;
import de.sirati97.bex_proto.v2.service.basic.BasicService;
import de.sirati97.bex_proto.v2.networkmodell.INetworkProtocol;
import de.sirati97.bex_proto.v2.networkmodell.INetworkStackImplementation;
import de.sirati97.bex_proto.v2.networkmodell.ServerBase;

import java.io.IOException;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.CompletionHandler;
import java.nio.channels.ShutdownChannelGroupException;
import java.util.HashSet;

import static de.sirati97.bex_proto.v2.networkmodell.CommonNetworkProtocols.TCP;
import static de.sirati97.bex_proto.v2.networkmodell.CommonNetworkStackImplementation.AsynchronousIO;

/**
 * Created by sirati97 on 17.04.2016.
 */
public class TcpAIOServer<Connection extends BasicService> extends ServerBase<Connection> {
    private final AsynchronousServerSocketChannel serverSocket;
    private final ITcpAddress address;
    private boolean listening = false;

    public TcpAIOServer(IServiceFactory<Connection> factory, ITcpAddress address) throws IOException {
        super(factory);
        this.serverSocket = AsynchronousServerSocketChannel.open();
        this.address = address;
    }

    @Override
    public synchronized void startListening() throws IOException{
        if (listening) {
            throw new IllegalStateException("Server was already started");
        }
        serverSocket.bind(address.getServer());
        listen();
    }

    private void listen() {
        serverSocket.accept(null, new CompletionHandler<AsynchronousSocketChannel, Object>() {
            @Override
            public void completed(AsynchronousSocketChannel socket, Object attachment) {
                try {
                    Connection connection = getFactory().createServerService(new TcpSocketAIOHandler(socket));
                    registerConnection(connection);
                    connection.expectConnection();
                } catch (Throwable e) {
                    if (e instanceof ShutdownChannelGroupException) {
                        return;
                    }
                    e.printStackTrace();
                }
                listen();
            }

            @Override
            public void failed(Throwable e, Object attachment) {
                if (!(e instanceof ShutdownChannelGroupException || e instanceof ClosedChannelException)) {
                    e.printStackTrace();
                }
                listen();
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
        return getFactory().getLogger().getLogger("TcpAIOServer{ip="+address.getServer().getAddress().getHostAddress()+", port="+address.getServer().getPort()+"}");
    }

    @Override
    public INetworkProtocol getUnderlyingProtocol() {
        return TCP;
    }

    @Override
    public INetworkStackImplementation getNetworkStackImplementation() {
        return AsynchronousIO;
    }
}
