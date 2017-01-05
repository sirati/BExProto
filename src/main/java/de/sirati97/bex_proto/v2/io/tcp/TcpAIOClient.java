package de.sirati97.bex_proto.v2.io.tcp;

import de.sirati97.bex_proto.builder.ITcpAddress;
import de.sirati97.bex_proto.v2.IConnectionServiceFactory;
import de.sirati97.bex_proto.v2.artifcon.ArtifConnectionService;
import de.sirati97.bex_proto.v2.networkmodell.ClientBase;
import de.sirati97.bex_proto.v2.networkmodell.INetworkProtocol;
import de.sirati97.bex_proto.v2.networkmodell.INetworkStackImplementation;

import java.io.IOException;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;

import static de.sirati97.bex_proto.v2.networkmodell.CommonNetworkProtocols.TCP;
import static de.sirati97.bex_proto.v2.networkmodell.CommonNetworkStackImplementation.AsynchronousIO;

/**
 * Created by sirati97 on 18.04.2016.
 */
public class TcpAIOClient<Connection extends ArtifConnectionService> extends ClientBase<Connection> {
    private final AsynchronousSocketChannel socket;
    private final ITcpAddress address;

    public TcpAIOClient(IConnectionServiceFactory<Connection> factory, String name, ITcpAddress address) throws IOException {
        super(factory, name);
        this.socket = AsynchronousSocketChannel.open();
        this.address = address;

    }

    @Override
    public synchronized void connect() throws InterruptedException, IOException, TimeoutException {
        if (getConnection() == null) {
            if (address.hasLocal()) {
                socket.bind(address.getLocal());
            }
            Future future = socket.connect(address.getServer());
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

    @Override
    public INetworkProtocol getUnderlyingProtocol() {
        return TCP;
    }

    @Override
    public INetworkStackImplementation getNetworkStackImplementation() {
        return AsynchronousIO;
    }
}
