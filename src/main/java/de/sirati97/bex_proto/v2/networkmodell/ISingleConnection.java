package de.sirati97.bex_proto.v2.networkmodell;

import de.sirati97.bex_proto.v2.artifcon.ArtifConnectionService;

/**
 * Created by sirati97 on 04.01.2017 for BexProto.
 */
public interface ISingleConnection<Connection extends ArtifConnectionService> extends IConnection<Connection> {
    Connection getConnection();

    String getName();
}
