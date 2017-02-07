package de.sirati97.bex_proto.v2.networkmodell;

import de.sirati97.bex_proto.v2.service.basic.BasicService;

/**
 * Created by sirati97 on 04.01.2017 for BexProto.
 */
public interface IPeer<Connection extends BasicService> extends ISingleConnection<Connection> {
}
