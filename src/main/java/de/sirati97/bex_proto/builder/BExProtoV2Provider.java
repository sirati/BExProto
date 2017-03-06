package de.sirati97.bex_proto.builder;

import de.sirati97.bex_proto.builder.internal.PExProtoIOFactoryHelper;
import de.sirati97.bex_proto.v2.IServiceFactory;
import de.sirati97.bex_proto.v2.service.basic.BasicService;
import de.sirati97.bex_proto.v2.networkmodel.IArchitectureFunction;
import de.sirati97.bex_proto.v2.networkmodel.IConnection;
import de.sirati97.bex_proto.v2.networkmodel.INetworkProtocol;
import de.sirati97.bex_proto.v2.networkmodel.INetworkStackImplementation;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static de.sirati97.bex_proto.v2.networkmodel.CommonArchitectureFunction.Client;
import static de.sirati97.bex_proto.v2.networkmodel.CommonArchitectureFunction.Server;
import static de.sirati97.bex_proto.v2.networkmodel.CommonNetworkProtocols.TCP;
import static de.sirati97.bex_proto.v2.networkmodel.CommonNetworkStackImplementation.AsynchronousIO;
import static de.sirati97.bex_proto.v2.networkmodel.CommonNetworkStackImplementation.BlockingIO;

/**
 * Created by sirati97 on 04.01.2017 for BexProto.
 */
public final class BExProtoV2Provider implements IProvider{
    private static final String ERROR_ADDRESS_WRONG_TYPE = "Address has to implement ITcpAddress to specify a tcp connection.";
    private static final String ERROR_LOCAL_ADDRESS = "This io handler implementation does not support a local address.";
    private static final String ERROR_NO_NAME_SET = "This io handler implementation requires a set name.";
    private static BExProtoV2Provider instance = new BExProtoV2Provider();

    public static BExProtoV2Provider getInstance() {
        return instance;
    }

    private BExProtoV2Provider() {
        registerBExIOHandler();
    }

    private void registerBExIOHandler() {
        //TcpBIOServer
        register(TCP, BlockingIO, Server, new Factory() {
            @Override
            public <ConnectionType extends BasicService> IConnection<ConnectionType> build(IServiceFactory<ConnectionType> factory, IAddress address, Options options) throws IOException {
                if (!(address instanceof ITcpAddress)) {
                    throw new IllegalArgumentException(ERROR_ADDRESS_WRONG_TYPE);
                }
                ITcpAddress tcpAddress = (ITcpAddress) address;
                if (tcpAddress.hasLocal()) {
                    throw new IllegalStateException(ERROR_LOCAL_ADDRESS);
                }
                return PExProtoIOFactoryHelper.instance.createTcpBIOServer(factory, tcpAddress);
            }
        });
        //TcpBIOClient
        register(TCP, BlockingIO, Client, new Factory() {
            @Override
            public <ConnectionType extends BasicService> IConnection<ConnectionType> build(IServiceFactory<ConnectionType> factory, IAddress address, Options options) throws IOException {
                if (!(address instanceof ITcpAddress)) {
                    throw new IllegalArgumentException(ERROR_ADDRESS_WRONG_TYPE);
                }
                if (!options.hasName()) {
                    throw new IllegalStateException(ERROR_NO_NAME_SET);
                }
                ITcpAddress tcpAddress = (ITcpAddress) address;
                return PExProtoIOFactoryHelper.instance.createTcpBIOClient(factory, options.getName(), tcpAddress);
            }
        });
        //TcpAIOServer
        register(TCP, AsynchronousIO, Server, new Factory() {
            @Override
            public <ConnectionType extends BasicService> IConnection<ConnectionType> build(IServiceFactory<ConnectionType> factory, IAddress address, Options options) throws IOException {
                if (!(address instanceof ITcpAddress)) {
                    throw new IllegalArgumentException(ERROR_ADDRESS_WRONG_TYPE);
                }
                ITcpAddress tcpAddress = (ITcpAddress) address;
                if (tcpAddress.hasLocal()) {
                    throw new IllegalStateException(ERROR_LOCAL_ADDRESS);
                }
                return PExProtoIOFactoryHelper.instance.createTcpAIOServer(factory, tcpAddress);
            }
        });
        //TcpAIOClient
        register(TCP, AsynchronousIO, Client, new Factory() {
            @Override
            public <ConnectionType extends BasicService> IConnection<ConnectionType> build(IServiceFactory<ConnectionType> factory, IAddress address, Options options) throws IOException {
                if (!(address instanceof ITcpAddress)) {
                    throw new IllegalArgumentException(ERROR_ADDRESS_WRONG_TYPE);
                }
                if (!options.hasName()) {
                    throw new IllegalStateException(ERROR_NO_NAME_SET);
                }
                ITcpAddress tcpAddress = (ITcpAddress) address;
                return PExProtoIOFactoryHelper.instance.createTcpAIOClient(factory, options.getName(), tcpAddress);
            }
        });
    }

    private Map<Key, Factory> map = new HashMap<>();


    public Factory register(INetworkProtocol underlyingProtocol, INetworkStackImplementation stackImplementation, IArchitectureFunction function, Factory factory) {
        Key key = new Key(underlyingProtocol, stackImplementation, function);
        Factory old = map.get(key);
        if (factory == null) {
            throw new IllegalStateException("Factory cannot be null");
        }
        map.put(key, factory);
        return old;
    }

    @Override
    public <ConnectionType extends BasicService> IConnection<ConnectionType> build(IServiceFactory<ConnectionType> factory, INetworkProtocol underlyingProtocol, INetworkStackImplementation stackImplementation, IArchitectureFunction function, IAddress address, Options options) throws IOException {
        Factory f = map.get(new Key(underlyingProtocol, stackImplementation, function));
        if (f == null) {
            throw new IllegalStateException("Cannot find builder for underlyingProtocol=" + underlyingProtocol.getName() + ", stackImplementation=" + underlyingProtocol.getName() + " and architectureFunction=" + function.getName());
        } else {
            return f.build(factory, address, options);
        }
    }

    public static class Key {
        private final INetworkProtocol protocol;
        private final INetworkStackImplementation stackImplementation;
        private final IArchitectureFunction function;

        public Key(INetworkProtocol protocol, INetworkStackImplementation stackImplementation, IArchitectureFunction function) {
            this.protocol = protocol;
            this.stackImplementation = stackImplementation;
            this.function = function;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof Key)) return false;
            Key key = (Key) obj;
            return protocol.equals(key.protocol) && stackImplementation.equals(key.stackImplementation) && function.equals(key.function);
        }

        @Override
        public int hashCode() {
            return Objects.hash(protocol.getName().toLowerCase(), stackImplementation.getName().toLowerCase(), function.getName().toLowerCase());
        }
    }

    public interface Factory {
        <ConnectionType extends BasicService> IConnection<ConnectionType> build(IServiceFactory<ConnectionType> factory, IAddress address, Options options) throws IOException;
    }
}
