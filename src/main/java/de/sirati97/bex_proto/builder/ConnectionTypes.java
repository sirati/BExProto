package de.sirati97.bex_proto.builder;

import de.sirati97.bex_proto.threading.AsyncHelper;
import de.sirati97.bex_proto.util.logging.ILogger;
import de.sirati97.bex_proto.v2.IConnectionServiceFactory;
import de.sirati97.bex_proto.v2.IPacketDefinition;
import de.sirati97.bex_proto.v2.artifcon.ArtifConnectionService;
import de.sirati97.bex_proto.v2.artifcon.ArtifConnectionServiceFactory;
import de.sirati97.bex_proto.v2.modular.ModularArtifConnectionService;
import de.sirati97.bex_proto.v2.modular.ModularArtifConnectionServiceFactory;

/**
 * Created by sirati97 on 05.01.2017 for BexProto.
 */
public abstract class ConnectionTypes<T extends ArtifConnectionService> implements IConnectionFactory<T> {
    private ConnectionTypes(){}
    public static final ConnectionTypes<ArtifConnectionService> UnmanagedConnection = new ConnectionTypes<ArtifConnectionService>() {
        @Override
        public IConnectionServiceFactory<ArtifConnectionService> buildConnectionServiceFactory(AsyncHelper asyncHelper, ILogger logger, IPacketDefinition packetProcessor, Options options) {
            return new ArtifConnectionServiceFactory(asyncHelper, logger, packetProcessor);
        }
    };
    public static final ConnectionTypes<ModularArtifConnectionService> ModularManagedConnection = new ConnectionTypes<ModularArtifConnectionService>() {
        @Override
        public IConnectionServiceFactory<ModularArtifConnectionService> buildConnectionServiceFactory(AsyncHelper asyncHelper, ILogger logger, IPacketDefinition packetProcessor, Options options) {
            return new ModularArtifConnectionServiceFactory(asyncHelper, logger, packetProcessor);
        }
    };
}
