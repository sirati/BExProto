package de.sirati97.bex_proto.builder;

import de.sirati97.bex_proto.v2.IServiceFactory;
import de.sirati97.bex_proto.v2.service.basic.BasicService;
import de.sirati97.bex_proto.v2.networkmodel.IArchitectureFunction;
import de.sirati97.bex_proto.v2.networkmodel.IConnection;
import de.sirati97.bex_proto.v2.networkmodel.INetworkProtocol;
import de.sirati97.bex_proto.v2.networkmodel.INetworkStackImplementation;

import java.io.IOException;

/**
 * Created by sirati97 on 04.01.2017 for BexProto.
 */
public interface IProvider {
    <ConnectionType extends BasicService> IConnection<ConnectionType> build(IServiceFactory<ConnectionType> factory, INetworkProtocol underlyingProtocol, INetworkStackImplementation stackImplementation, IArchitectureFunction function, IAddress address, Options options) throws IOException;
}
