package de.sirati97.bex_proto.v2.networkmodel;

/**
 * Created by sirati97 on 04.01.2017 for BexProto.
 */
public enum CommonNetworkStackImplementation implements INetworkStackImplementation{
    BlockingIO,
    NonBlockingIO,
    AsynchronousIO;

    @Override
    public String getName() {
        return super.toString();
    }

    @Override
    public boolean equals(INetworkStackImplementation obj) {
        return getName().equalsIgnoreCase(obj.getName());
    }
}
