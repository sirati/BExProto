package de.sirati97.bex_proto.v2.io.tcp;

import de.sirati97.bex_proto.datahandler.InetAddressPort;
import de.sirati97.bex_proto.v2.ClientBase;
import de.sirati97.bex_proto.v2.IConnectionFactory;
import de.sirati97.bex_proto.v2.artifcon.ArtifConnection;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.TimeoutException;

/**
 * Created by sirati97 on 18.04.2016.
 */
public class TcpBIOClient<Connection extends ArtifConnection> extends ClientBase<Connection> {
    private final Socket socket;

    protected TcpBIOClient(IConnectionFactory<Connection> factory, String name, Socket socket) {
        super(factory, name);
        this.socket = socket;
    }

    protected TcpBIOClient(IConnectionFactory<Connection> factory, String name, String host, int port) throws IOException {
        this(factory, name, new Socket(host, port));
    }

    public TcpBIOClient(IConnectionFactory<Connection> factory, String name, InetAddress address, int port) throws IOException {
        this(factory, name, new Socket(address, port));
    }

    public TcpBIOClient(IConnectionFactory<Connection> factory, String name, InetAddressPort inetAddressPort) throws IOException {
        this(factory, name, inetAddressPort.getInetAddress(), inetAddressPort.getPort());
    }

    public TcpBIOClient(IConnectionFactory<Connection> factory, String name, InetSocketAddress socketAddress) throws IOException {
        this(factory, name, socketAddress.getAddress(), socketAddress.getPort());
    }

    public TcpBIOClient(IConnectionFactory<Connection> factory, String name, InetAddress address, int port, InetAddress localAddress, int localPort) throws IOException {
        this(factory, name, new Socket(address, port, localAddress, localPort));
    }

    public TcpBIOClient(IConnectionFactory<Connection> factory, String name, InetAddressPort inetAddressPort, InetAddressPort localInetAddressPort) throws IOException {
        this(factory, name, inetAddressPort.getInetAddress(), inetAddressPort.getPort(), localInetAddressPort.getInetAddress(), localInetAddressPort.getPort());
    }

    public TcpBIOClient(IConnectionFactory<Connection> factory, String name, InetSocketAddress socketAddress, InetSocketAddress localSocketAddress) throws IOException {
        this(factory, name, socketAddress.getAddress(), socketAddress.getPort(), localSocketAddress.getAddress(), localSocketAddress.getPort());
    }

    @Override
    public synchronized void connect() throws InterruptedException, IOException, TimeoutException {
        if (getConnection() == null) {
            setConnection(getFactory().createClient(getName(), new TcpSocketBIOHandler(socket)));
        }
        getConnection().connect();
    }

    @Override
    public boolean isConnected() {
        return super.isConnected() &&socket.isConnected();
    }
}
