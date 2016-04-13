package de.sirati97.bex_proto.v2.module;

import de.sirati97.bex_proto.v2.IPacket;

/**
 * Created by sirati97 on 13.04.2016.
 */
public interface IModule {
    short getId();
    IPacket getPacket();
    Object createData(ModularArtifConnection connection);
}
