package de.sirati97.bex_proto.builder;

import de.sirati97.bex_proto.v2.IServiceFactory;
import de.sirati97.bex_proto.v2.service.basic.BasicService;
import de.sirati97.bex_proto.v2.networkmodell.IArchitectureFunction;
import de.sirati97.bex_proto.v2.networkmodell.IConnection;
import de.sirati97.bex_proto.v2.networkmodell.INetworkProtocol;
import de.sirati97.bex_proto.v2.networkmodell.INetworkStackImplementation;

import java.io.IOException;

/**
 * Created by sirati97 on 04.01.2017 for BexProto.
 */
public interface IProvider {
    <ConnectionType extends BasicService> IConnection<ConnectionType> build(IServiceFactory<ConnectionType> factory, INetworkProtocol underlyingProtocol, INetworkStackImplementation stackImplementation, IArchitectureFunction function, IAddress address, Options options) throws IOException;
}
