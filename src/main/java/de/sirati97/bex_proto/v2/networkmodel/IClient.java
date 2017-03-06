package de.sirati97.bex_proto.v2.networkmodel;

import de.sirati97.bex_proto.v2.service.basic.BasicService;

/**
 * Created by sirati97 on 04.01.2017 for BexProto.
 */
public interface IClient<Connection extends BasicService> extends ISingleConnection<Connection> {
}
