package de.sirati97.bex_proto.test.v2;

import de.sirati97.bex_proto.v2.IPacketDefinition;
import de.sirati97.bex_proto.v2.PacketCollection;
import de.sirati97.bex_proto.v2.modular.IModuleHandshake;
import de.sirati97.bex_proto.v2.modular.ModularArtifConnectionService;
import de.sirati97.bex_proto.v2.modular.Module;
import de.sirati97.bex_proto.v2.modular.internal.ICallback;
import de.sirati97.bex_proto.v2.modular.internal.YieldCause;

/**
 * Created by sirati97 on 13.04.2016.
 */
public class RemoteYieldModule extends Module implements IModuleHandshake{
    public RemoteYieldModule() {
        super((short) 2);
    }

    @Override
    protected IPacketDefinition createPacket() {
        return new PacketCollection();
    }

    @Override
    public Object createData(ModularArtifConnectionService connection) {
        return null;
    }

    @Override
    public void onHandshake(ModularArtifConnectionService connection, final ICallback callback) {
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
    public void onHandshakeServerSide(ModularArtifConnectionService connection, ICallback callback) throws Throwable {

    }

    @Override
    public boolean completeHandshake(ModularArtifConnectionService connection) throws Throwable {
        return true;
    }

    @Override
    public boolean hasHighHandshakePriority() {
        return false;
    }
}
