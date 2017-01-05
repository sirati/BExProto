package de.sirati97.bex_proto.builder;

import java.net.InetSocketAddress;

/**
 * Created by sirati97 on 05.01.2017 for BexProto.
 */
public interface ITcpAddress extends IAddress {
    InetSocketAddress getLocal();

    InetSocketAddress getServer();

    boolean hasLocal();
}
