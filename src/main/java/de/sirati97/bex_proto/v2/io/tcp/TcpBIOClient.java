package de.sirati97.bex_proto.v2.io.tcp;

import de.sirati97.bex_proto.builder.ITcpAddress;
import de.sirati97.bex_proto.v2.IConnectionServiceFactory;
import de.sirati97.bex_proto.v2.artifcon.ArtifConnectionService;
import de.sirati97.bex_proto.v2.networkmodell.ClientBase;
import de.sirati97.bex_proto.v2.networkmodell.INetworkProtocol;
import de.sirati97.bex_proto.v2.networkmodell.INetworkStackImplementation;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.TimeoutException;

import static de.sirati97.bex_proto.v2.networkmodell.CommonNetworkProtocols.TCP;
import static de.sirati97.bex_proto.v2.networkmodell.CommonNetworkStackImplementation.BlockingIO;

/**
 * Created by sirati97 on 18.04.2016.
 */
public class TcpBIOClient<Connection extends ArtifConnectionService> extends ClientBase<Connection> {
    private final Socket socket = new Socket();
    private final ITcpAddress address;

    public TcpBIOClient(IConnectionServiceFactory<Connection> factory, String name, ITcpAddress address) {
        super(factory, name);
        this.address = address;
    }


    @Override
    public synchronized void connect() throws InterruptedException, IOException, TimeoutException {
        if (getConnection() == null) {
            if (address.hasLocal()) {
                socket.bind(address.getLocal());
            }
            socket.connect(address.getServer());
            setConnection(getFactory().createClient(getName(), new TcpSocketBIOHandler(socket)));
        }
        getConnection().connect();
    }

    @Override
    public boolean isConnected() {
        return super.isConnected() &&socket.isConnected();
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
