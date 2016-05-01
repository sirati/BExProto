package de.sirati97.bex_proto.v2.io.tcp;

import de.sirati97.bex_proto.datahandler.InetAddressPort;
import de.sirati97.bex_proto.v2.ClientBase;
import de.sirati97.bex_proto.v2.IConnectionFactory;
import de.sirati97.bex_proto.v2.artifcon.ArtifConnection;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;

/**
 * Created by sirati97 on 18.04.2016.
 */
public class TcpAIOClient<Connection extends ArtifConnection> extends ClientBase<Connection> {
    private final AsynchronousSocketChannel socket;
    private final InetSocketAddress socketAddress;

    protected TcpAIOClient(IConnectionFactory<Connection> factory, String name, String host, int port) throws IOException {
        this(factory, name, new InetSocketAddress(host, port));
    }

    public TcpAIOClient(IConnectionFactory<Connection> factory, String name, InetAddress address, int port) throws IOException {
        this(factory, name, new InetSocketAddress(address, port));
    }

    public TcpAIOClient(IConnectionFactory<Connection> factory, String name, InetAddressPort inetAddressPort) throws IOException {
        this(factory, name, inetAddressPort.toInetSocketAddress());
    }

    public TcpAIOClient(IConnectionFactory<Connection> factory, String name, InetSocketAddress socketAddress) throws IOException {
        super(factory, name);
        this.socket = AsynchronousSocketChannel.open();
        this.socketAddress = socketAddress;

    }

    @Override
    public synchronized void connect() throws InterruptedException, IOException, TimeoutException {
        if (getConnection() == null) {
            Future future = socket.connect(socketAddress);
            try {
                future.get();
            } catch (ExecutionException e) {
                throw new IOException(e);
            }
            setConnection(getFactory().createClient(getName(), new TcpSocketAIOHandler(socket)));
        }
        getConnection().connect();
    }

    @Override
    public boolean isConnected() {
        return super.isConnected() &&socket.isOpen();
    }
}
