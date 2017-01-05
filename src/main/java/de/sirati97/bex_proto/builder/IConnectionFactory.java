package de.sirati97.bex_proto.builder;

import de.sirati97.bex_proto.threading.AsyncHelper;
import de.sirati97.bex_proto.util.logging.ILogger;
import de.sirati97.bex_proto.v2.IConnectionServiceFactory;
import de.sirati97.bex_proto.v2.IPacketDefinition;
import de.sirati97.bex_proto.v2.artifcon.ArtifConnectionService;

/**
 * Created by sirati97 on 05.01.2017 for BexProto.
 */
public interface IConnectionFactory<T extends ArtifConnectionService> {
    IConnectionServiceFactory<T> buildConnectionServiceFactory(AsyncHelper asyncHelper, ILogger logger, IPacketDefinition packetProcessor, Options options);
}
