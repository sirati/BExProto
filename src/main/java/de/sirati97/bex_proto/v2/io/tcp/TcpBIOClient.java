package de.sirati97.bex_proto.v2.io.tcp;

import de.sirati97.bex_proto.builder.ITcpAddress;
import de.sirati97.bex_proto.v2.IServiceFactory;
import de.sirati97.bex_proto.v2.service.basic.BasicService;
import de.sirati97.bex_proto.v2.networkmodel.ClientBase;
import de.sirati97.bex_proto.v2.networkmodel.INetworkProtocol;
import de.sirati97.bex_proto.v2.networkmodel.INetworkStackImplementation;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.TimeoutException;

import static de.sirati97.bex_proto.v2.networkmodel.CommonNetworkProtocols.TCP;
import static de.sirati97.bex_proto.v2.networkmodel.CommonNetworkStackImplementation.BlockingIO;

/**
 * Created by sirati97 on 18.04.2016.
 */
public class TcpBIOClient<Connection extends BasicService> extends ClientBase<Connection> {
    private final Socket socket = new Socket();
    private final ITcpAddress address;

    public TcpBIOClient(IServiceFactory<Connection> factory, String name, ITcpAddress address) {
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
            setConnection(getFactory().createClientService(getName(), new TcpSocketBIOHandler(socket)));
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
