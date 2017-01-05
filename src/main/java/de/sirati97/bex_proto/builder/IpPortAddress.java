package de.sirati97.bex_proto.builder;

import de.sirati97.bex_proto.datahandler.InetAddressPort;

import java.net.InetAddress;
import java.net.InetSocketAddress;

/**
 * Created by sirati97 on 05.01.2017 for BexProto.
 */
public class IpPortAddress implements ITcpAddress {
    private final InetSocketAddress server;
    private final InetSocketAddress local;

    public IpPortAddress(InetSocketAddress server, InetSocketAddress local) {
        this.server = server;
        this.local = local;
    }

    public IpPortAddress(InetSocketAddress server) {
        this(server, null);
    }

    public IpPortAddress(InetAddress address, int port) {
        this(new InetSocketAddress(address, port));
    }

    public IpPortAddress(int port) {
        this(null, port);
    }

    public IpPortAddress(InetAddressPort inetAddressPort) {
        this(inetAddressPort.toInetSocketAddress());
    }

    public IpPortAddress(InetAddress serverAddress, int serverPort, InetAddress localAddress, int localPort) {
        this(new InetSocketAddress(serverAddress, serverPort), new InetSocketAddress(localAddress, localPort));
    }


    public IpPortAddress(int serverPort, InetAddress localAddress, int localPort) {
        this(null, serverPort, localAddress, localPort);
    }

    public IpPortAddress(InetAddress serverAddress, int serverPort, int localPort) {
        this(serverAddress, serverPort, null, localPort);
    }

    public IpPortAddress(int serverPort, int localPort) {
        this(null , serverPort, null, localPort);
    }

    public IpPortAddress(InetAddressPort server, InetAddressPort local) {
        this(server.toInetSocketAddress(), local.toInetSocketAddress());
    }

    @Override
    public InetSocketAddress getLocal() {
        return local;
    }

    @Override
    public InetSocketAddress getServer() {
        return server;
    }

    @Override
    public boolean hasLocal() {
        return local!=null;
    }
}
