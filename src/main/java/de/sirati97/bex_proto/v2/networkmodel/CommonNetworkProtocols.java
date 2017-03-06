package de.sirati97.bex_proto.v2.networkmodel;

/**
 * Created by sirati97 on 04.01.2017 for BexProto.
 */
public enum CommonNetworkProtocols implements INetworkProtocol{
    TCP,
    UDP;

    @Override
    public String getName() {
        return super.toString();
    }

    @Override
    public boolean equals(INetworkProtocol obj) {
        return getName().equalsIgnoreCase(obj.getName());
    }
}
