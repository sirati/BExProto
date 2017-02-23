package de.sirati97.bex_proto.builder;

import de.sirati97.bex_proto.threading.IAsyncHelper;
import de.sirati97.bex_proto.util.logging.ILogger;
import de.sirati97.bex_proto.v2.IServiceFactory;
import de.sirati97.bex_proto.v2.IPacketDefinition;
import de.sirati97.bex_proto.v2.service.basic.BasicService;

/**
 * Created by sirati97 on 05.01.2017 for BexProto.
 */
public interface IServiceType<T extends BasicService> {
    IServiceFactory<T> buildServiceFactory(IAsyncHelper asyncHelper, ILogger logger, IPacketDefinition packetProcessor, Options options);
}
