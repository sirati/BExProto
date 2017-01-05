package de.sirati97.bex_proto.builder;

import de.sirati97.bex_proto.threading.AsyncHelper;
import de.sirati97.bex_proto.threading.ThreadPoolAsyncHelper;
import de.sirati97.bex_proto.util.logging.ILogger;
import de.sirati97.bex_proto.util.logging.SysOutLogger;
import de.sirati97.bex_proto.v2.IConnectionServiceFactory;
import de.sirati97.bex_proto.v2.IPacketDefinition;
import de.sirati97.bex_proto.v2.artifcon.ArtifConnectionService;
import de.sirati97.bex_proto.v2.networkmodell.IArchitectureFunction;
import de.sirati97.bex_proto.v2.networkmodell.IClient;
import de.sirati97.bex_proto.v2.networkmodell.IConnection;
import de.sirati97.bex_proto.v2.networkmodell.INetworkProtocol;
import de.sirati97.bex_proto.v2.networkmodell.INetworkStackImplementation;
import de.sirati97.bex_proto.v2.networkmodell.IPeer;
import de.sirati97.bex_proto.v2.networkmodell.IServer;

import java.io.IOException;

import static de.sirati97.bex_proto.v2.networkmodell.CommonArchitectureFunction.*;
import static de.sirati97.bex_proto.v2.networkmodell.CommonNetworkProtocols.TCP;
import static de.sirati97.bex_proto.v2.networkmodell.CommonNetworkStackImplementation.AsynchronousIO;

/**
 * Created by sirati97 on 04.01.2017 for BexProto.
 */
public class Builder<ConnectionType extends ArtifConnectionService> {
    private IConnectionFactory<ConnectionType> factory;
    private IConnectionServiceFactory<ConnectionType> serviceFactory;
    private INetworkProtocol underlyingProtocol = TCP;
    private INetworkStackImplementation stackImplementation = AsynchronousIO;
    private IProvider provider = BExProtoV2Provider.getInstance();
    private AsyncHelper asyncHelper;
    private ILogger logger;
    private IPacketDefinition packetProcessor;
    private Options serviceFactoryOptions = new Options();

    public Builder(ConnectionTypes<ConnectionType> factory, IPacketDefinition packetProcessor) {
        this((IConnectionFactory<ConnectionType>)factory, packetProcessor);
    }

    public Builder(IConnectionFactory<ConnectionType> factory, IPacketDefinition packetProcessor) {
        this.factory = factory;
        this.packetProcessor = packetProcessor;
    }

    public Builder stackImplementation(INetworkStackImplementation stackImplementation) {
        check(stackImplementation);
        this.stackImplementation = stackImplementation;
        return this;
    }

    private void check(Object obj) {
        if (serviceFactory != null) {
            throw new IllegalStateException("Builder cannot be modified after it was frozen");
        }
        if (stackImplementation==null) {
            throw new IllegalArgumentException("Argument cannot be null");
        }
    }

    public Builder underlyingProtocol(INetworkProtocol underlyingProtocol) {
        check(underlyingProtocol);
        this.underlyingProtocol = underlyingProtocol;
        return this;
    }

    public Builder factory(IConnectionFactory<ConnectionType> factory) {
        check(factory);
        this.factory = factory;
        return this;
    }

    public Builder provider(IProvider provider) {
        check(provider);
        this.provider = provider;
        return this;
    }

    public Builder logger(ILogger logger) {
        check("");
        this.logger = logger;
        return this;
    }

    public Builder asyncHelper(AsyncHelper asyncHelper) {
        check("");
        this.asyncHelper = asyncHelper;
        return this;
    }

    public Builder packetProcessor(IPacketDefinition packetProcessor) {
        check(packetProcessor);
        this.packetProcessor = packetProcessor;
        return this;
    }

    public Builder serviceFactoryOptions(Options serviceFactoryOptions) {
        check(serviceFactoryOptions);
        this.serviceFactoryOptions = serviceFactoryOptions;
        return this;
    }

    public IConnectionFactory<ConnectionType> getFactory() {
        return factory;
    }

    public INetworkProtocol getUnderlyingProtocol() {
        return underlyingProtocol;
    }

    public INetworkStackImplementation getStackImplementation() {
        return stackImplementation;
    }

    public IProvider getProvider() {
        return provider;
    }

    public AsyncHelper getAsyncHelper() {
        return asyncHelper;
    }

    public ILogger getLogger() {
        return logger;
    }

    public IPacketDefinition getPacketProcessor() {
        return packetProcessor;
    }

    public Options getServiceFactoryOptions() {
        return serviceFactoryOptions;
    }

    private void initServiceFactory() {
        logger = logger==null?new SysOutLogger():logger;
        asyncHelper = asyncHelper==null?new ThreadPoolAsyncHelper():asyncHelper;
        serviceFactory = factory.buildConnectionServiceFactory(asyncHelper, logger, packetProcessor, serviceFactoryOptions);
    }
    
    public <T extends IConnection<ConnectionType>> T buildCustom(IAddress address, Options options, IArchitectureFunction function) throws IOException {
        if (serviceFactory == null) {
            initServiceFactory();
        }
        options = options==null?new Options():options;
        //noinspection unchecked
        return (T) provider.build(serviceFactory, underlyingProtocol, stackImplementation, function, address, options);
    }

    public <T extends IConnection<ConnectionType>> T buildCustom(IAddress address, IArchitectureFunction function) throws IOException {
        return buildCustom(address, null, function);
    }

    public IServer<ConnectionType> buildServer(IAddress address, Options options) throws IOException {
        return buildCustom(address, options, Server);
    }

    public IServer<ConnectionType> buildServer(IAddress address) throws IOException {
        return buildServer(address, null);
    }

    public IClient<ConnectionType> buildClient(IAddress address, Options options) throws IOException {
        return buildCustom(address, options, Client);
    }

    public IClient<ConnectionType> buildClient(IAddress address) throws IOException {
        return buildClient(address, (Options) null);
    }

    public IClient<ConnectionType> buildClient(IAddress address, String name) throws IOException {
        return buildClient(address, new Options().name(name));
    }

    public IClient<ConnectionType> buildClient(IAddress address, String name, Options options) throws IOException {
        return buildClient(address, options.name(name));
    }

    public IPeer<ConnectionType> buildPeer(IAddress address, Options options) throws IOException {
        return buildCustom(address, options, Peer);
    }

    public IPeer<ConnectionType> buildPeer(IAddress address) throws IOException {
        return buildPeer(address, (Options) null);
    }

    public IPeer<ConnectionType> buildPeer(IAddress address, String name) throws IOException {
        return buildPeer(address, new Options().name(name));
    }

    public IPeer<ConnectionType> buildPeer(IAddress address, String name, Options options) throws IOException {
        return buildPeer(address, options.name(name));
    }
}
