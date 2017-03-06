package de.sirati97.bex_proto.v2.networkmodel;

import de.sirati97.bex_proto.v2.service.basic.BasicService;

/**
 * Created by sirati97 on 04.01.2017 for BexProto.
 */
public interface ISingleConnection<Connection extends BasicService> extends IConnection<Connection> {
    Connection getConnection();

    String getName();
}
