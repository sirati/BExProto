package de.sirati97.bex_proto.test.v2.packettest;

import de.sirati97.bex_proto.v2.IPacket;
import de.sirati97.bex_proto.v2.PacketCollection;
import de.sirati97.bex_proto.v2.module.IModuleHandshake;
import de.sirati97.bex_proto.v2.module.ModularArtifConnection;
import de.sirati97.bex_proto.v2.module.Module;
import de.sirati97.bex_proto.v2.module.internal.IHandshakeCallback;

/**
 * Created by sirati97 on 13.04.2016.
 */
public class FailModule extends Module implements IModuleHandshake{
    public FailModule() {
        super((short) 0);
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
    public void onHandshake(ModularArtifConnection connection, final IHandshakeCallback callback) {
        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(100);
                    callback.error(new IllegalAccessException("THIS CANNOT HAPPEN"));
                } catch (InterruptedException e) {
                    callback.error(e);
                }
            }
        };
        t.start();
    }

    @Override
    public boolean hasHighHandshakePriority() {
        return false;
    }
}
