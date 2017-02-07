package de.sirati97.bex_proto.builder.internal;

import de.sirati97.bex_proto.builder.ITcpAddress;
import de.sirati97.bex_proto.v2.IServiceFactory;
import de.sirati97.bex_proto.v2.service.basic.BasicService;
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

    public <Connection extends BasicService> TcpAIOClient<Connection> createTcpAIOClient(IServiceFactory<Connection> factory, String name, ITcpAddress address) throws IOException {
        return new TcpAIOClient<>(factory, name, address);
    }

    public <Connection extends BasicService> TcpAIOServer<Connection> createTcpAIOServer(IServiceFactory<Connection> factory, ITcpAddress address) throws IOException {
        return new TcpAIOServer<>(factory, address);
    }

    public <Connection extends BasicService> TcpBIOClient<Connection> createTcpBIOClient(IServiceFactory<Connection> factory, String name, ITcpAddress address) throws IOException {
        return new TcpBIOClient<>(factory, name, address);
    }

    public <Connection extends BasicService> TcpBIOServer<Connection> createTcpBIOServer(IServiceFactory<Connection> factory, ITcpAddress address) throws IOException {
        return new TcpBIOServer<>(factory, address);
    }

}
