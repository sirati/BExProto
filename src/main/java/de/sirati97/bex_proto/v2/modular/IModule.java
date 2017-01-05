package de.sirati97.bex_proto.v2.modular;

import de.sirati97.bex_proto.v2.IPacketDefinition;

/**
 * Created by sirati97 on 13.04.2016.
 */
public interface IModule {
    short getId();
    IPacketDefinition getPacket();
    Object createData(ModularArtifConnectionService connection);
}
