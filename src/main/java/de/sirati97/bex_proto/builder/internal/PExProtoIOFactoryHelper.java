package de.sirati97.bex_proto.builder.internal;

import de.sirati97.bex_proto.builder.ITcpAddress;
import de.sirati97.bex_proto.v2.IConnectionServiceFactory;
import de.sirati97.bex_proto.v2.artifcon.ArtifConnectionService;
import de.sirati97.bex_proto.v2.io.tcp.TcpAIOClient;
import de.sirati97.bex_proto.v2.io.tcp.TcpAIOServer;
import de.sirati97.bex_proto.v2.io.tcp.TcpBIOClient;
import de.sirati97.bex_proto.v2.io.tcp.TcpBIOServer;

import java.io.IOException;

/**
 * Created by sirati97 on 05.01.2017 for BexProto.
 */
public class PExProtoIOFactoryHelper {
    public static PExProtoIOFactoryHelper instance = new PExProtoIOFactoryHelper();

    public <Connection extends ArtifConnectionService> TcpAIOClient<Connection> createTcpAIOClient(IConnectionServiceFactory<Connection> factory, String name, ITcpAddress address) throws IOException {
        return new TcpAIOClient<>(factory, name, address);
    }

    public <Connection extends ArtifConnectionService> TcpAIOServer<Connection> createTcpAIOServer(IConnectionServiceFactory<Connection> factory, ITcpAddress address) throws IOException {
        return new TcpAIOServer<>(factory, address);
    }

    public <Connection extends ArtifConnectionService> TcpBIOClient<Connection> createTcpBIOClient(IConnectionServiceFactory<Connection> factory, String name, ITcpAddress address) throws IOException {
        return new TcpBIOClient<>(factory, name, address);
    }

    public <Connection extends ArtifConnectionService> TcpBIOServer<Connection> createTcpBIOServer(IConnectionServiceFactory<Connection> factory, ITcpAddress address) throws IOException {
        return new TcpBIOServer<>(factory, address);
    }

}
