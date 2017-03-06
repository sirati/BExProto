package de.sirati97.bex_proto.builder;

import de.sirati97.bex_proto.threading.IAsyncHelper;
import de.sirati97.bex_proto.threading.ShutdownBehavior;
import de.sirati97.bex_proto.threading.ThreadPoolAsyncHelper;
import de.sirati97.bex_proto.util.logging.ILogger;
import de.sirati97.bex_proto.util.logging.SysOutLogger;
import de.sirati97.bex_proto.v2.IServiceFactory;
import de.sirati97.bex_proto.v2.IPacketDefinition;
import de.sirati97.bex_proto.v2.service.basic.BasicService;
import de.sirati97.bex_proto.v2.networkmodel.IArchitectureFunction;
import de.sirati97.bex_proto.v2.networkmodel.IClient;
import de.sirati97.bex_proto.v2.networkmodel.IConnection;
import de.sirati97.bex_proto.v2.networkmodel.INetworkProtocol;
import de.sirati97.bex_proto.v2.networkmodel.INetworkStackImplementation;
import de.sirati97.bex_proto.v2.networkmodel.IPeer;
import de.sirati97.bex_proto.v2.networkmodel.IServer;

import java.io.IOException;

import static de.sirati97.bex_proto.v2.networkmodel.CommonArchitectureFunction.*;
import static de.sirati97.bex_proto.v2.networkmodel.CommonNetworkProtocols.TCP;
import static de.sirati97.bex_proto.v2.networkmodel.CommonNetworkStackImplementation.AsynchronousIO;

/**
 * Created by sirati97 on 04.01.2017 for BexProto.
 */
public class BExBuilder<ConnectionType extends BasicService> {
    private IServiceType<ConnectionType> factory;
    private IServiceFactory<ConnectionType> serviceFactory;
    private INetworkProtocol underlyingProtocol = TCP;
    private INetworkStackImplementation stackImplementation = AsynchronousIO;
    private IProvider provider = BExProtoV2Provider.getInstance();
    private IAsyncHelper asyncHelper;
    private ILogger logger;
    private IPacketDefinition packetProcessor;
    private Options serviceFactoryOptions = new Options();

    public BExBuilder(ServiceTypes<ConnectionType> factory, IPacketDefinition packetProcessor) {
        this((IServiceType<ConnectionType>)factory, packetProcessor);
    }

    public BExBuilder(IServiceType<ConnectionType> factory, IPacketDefinition packetProcessor) {
        this.factory = factory;
        this.packetProcessor = packetProcessor;
    }

    public BExBuilder stackImplementation(INetworkStackImplementation stackImplementation) {
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

    public BExBuilder underlyingProtocol(INetworkProtocol underlyingProtocol) {
        check(underlyingProtocol);
        this.underlyingProtocol = underlyingProtocol;
        return this;
    }

    public BExBuilder factory(IServiceType<ConnectionType> factory) {
        check(factory);
        this.factory = factory;
        return this;
    }

    public BExBuilder provider(IProvider provider) {
        check(provider);
        this.provider = provider;
        return this;
    }

    public BExBuilder logger(ILogger logger) {
        check("");
        this.logger = logger;
        return this;
    }

    public BExBuilder asyncHelper(IAsyncHelper asyncHelper) {
        check("");
        this.asyncHelper = asyncHelper;
        return this;
    }

    public BExBuilder packetProcessor(IPacketDefinition packetProcessor) {
        check(packetProcessor);
        this.packetProcessor = packetProcessor;
        return this;
    }

    public BExBuilder serviceFactoryOptions(Options serviceFactoryOptions) {
        check(serviceFactoryOptions);
        this.serviceFactoryOptions = serviceFactoryOptions;
        return this;
    }

    public IServiceType<ConnectionType> getFactory() {
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

    public IAsyncHelper getAsyncHelper() {
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
        asyncHelper = asyncHelper==null?new ThreadPoolAsyncHelper(ShutdownBehavior.JavaVMShutdownNow):asyncHelper;
        serviceFactory = factory.buildServiceFactory(asyncHelper, logger, packetProcessor, serviceFactoryOptions);
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
