package de.sirati97.bex_proto.v2.service;

import de.sirati97.bex_proto.builder.Options;
import de.sirati97.bex_proto.threading.IAsyncHelper;
import de.sirati97.bex_proto.util.logging.ILogger;
import de.sirati97.bex_proto.v2.IServiceFactory;
import de.sirati97.bex_proto.v2.IPacketDefinition;
import de.sirati97.bex_proto.v2.service.basic.BasicService;
import de.sirati97.bex_proto.v2.service.basic.ArtifServiceFactory;
import de.sirati97.bex_proto.v2.service.modular.ModularServiceFactory;

/**
 * Created by sirati97 on 05.01.2017 for BexProto.
 */
public abstract class ServiceTypes<T extends BasicService> implements IServiceType<T> {
    private ServiceTypes(){}
    public static final ServiceTypes<de.sirati97.bex_proto.v2.service.basic.BasicService> BasicService = new ServiceTypes<de.sirati97.bex_proto.v2.service.basic.BasicService>() {
        @Override
        public IServiceFactory<de.sirati97.bex_proto.v2.service.basic.BasicService> buildServiceFactory(IAsyncHelper asyncHelper, ILogger logger, IPacketDefinition packetProcessor, Options options) {
            return new ArtifServiceFactory(asyncHelper, logger, packetProcessor);
        }
    };
    public static final ServiceTypes<de.sirati97.bex_proto.v2.service.modular.ModularService> ModularService = new ServiceTypes<de.sirati97.bex_proto.v2.service.modular.ModularService>() {
        @Override
        public IServiceFactory<de.sirati97.bex_proto.v2.service.modular.ModularService> buildServiceFactory(IAsyncHelper asyncHelper, ILogger logger, IPacketDefinition packetProcessor, Options options) {
            return new ModularServiceFactory(asyncHelper, logger, packetProcessor);
        }
    };
}
