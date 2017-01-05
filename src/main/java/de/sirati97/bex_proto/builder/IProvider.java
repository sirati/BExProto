package de.sirati97.bex_proto.builder;

import de.sirati97.bex_proto.v2.IConnectionServiceFactory;
import de.sirati97.bex_proto.v2.artifcon.ArtifConnectionService;
import de.sirati97.bex_proto.v2.networkmodell.IArchitectureFunction;
import de.sirati97.bex_proto.v2.networkmodell.IConnection;
import de.sirati97.bex_proto.v2.networkmodell.INetworkProtocol;
import de.sirati97.bex_proto.v2.networkmodell.INetworkStackImplementation;

import java.io.IOException;

/**
 * Created by sirati97 on 04.01.2017 for BexProto.
 */
public interface IProvider {
    <ConnectionType extends ArtifConnectionService> IConnection<ConnectionType> build(IConnectionServiceFactory<ConnectionType> factory, INetworkProtocol underlyingProtocol, INetworkStackImplementation stackImplementation, IArchitectureFunction function, IAddress address, Options options) throws IOException;
}
