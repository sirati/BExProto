package de.sirati97.bex_proto.test.v2.packettest;

import de.sirati97.bex_proto.v2.IPacket;
import de.sirati97.bex_proto.v2.PacketCollection;
import de.sirati97.bex_proto.v2.module.IModuleHandshake;
import de.sirati97.bex_proto.v2.module.ModularArtifConnection;
import de.sirati97.bex_proto.v2.module.Module;
import de.sirati97.bex_proto.v2.module.internal.ICallback;
import de.sirati97.bex_proto.v2.module.internal.YieldCause;

/**
 * Created by sirati97 on 13.04.2016.
 */
public class RemoteYieldModule extends Module implements IModuleHandshake{
    public RemoteYieldModule() {
        super((short) 2);
    }

    @Override
    protected IPacket createPacket() {
        return new PacketCollection();
    }

    @Override
    public Object createData(ModularArtifConnection connection) {
        return null;
    }

    @Override
    public void onHandshake(ModularArtifConnection connection, final ICallback callback) {
        for(int i=0;i<10;i++) {
            callback.yield(YieldCause.KeepAlive);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        callback.callback();
    }

    @Override
    public void onHandshakeServerSide(ModularArtifConnection connection, ICallback callback) throws Throwable {

    }

    @Override
    public boolean completeHandshake(ModularArtifConnection connection) throws Throwable {
        return true;
    }

    @Override
    public boolean hasHighHandshakePriority() {
        return false;
    }
}
