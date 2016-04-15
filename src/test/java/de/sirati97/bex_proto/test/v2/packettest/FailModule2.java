package de.sirati97.bex_proto.test.v2.packettest;

import de.sirati97.bex_proto.v2.IPacket;
import de.sirati97.bex_proto.v2.PacketCollection;
import de.sirati97.bex_proto.v2.module.IModuleHandshake;
import de.sirati97.bex_proto.v2.module.ModularArtifConnection;
import de.sirati97.bex_proto.v2.module.internal.IHandshakeCallback;
import de.sirati97.bex_proto.v2.module.internal.InternalModule;

/**
 * Created by sirati97 on 13.04.2016.
 */
public class FailModule2 extends InternalModule implements IModuleHandshake{
    public FailModule2() {
        super((short) -99);
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

    }

    @Override
    public void onHandshakeServerSide(ModularArtifConnection connection, IHandshakeCallback callback) throws Throwable {
        throw  new IllegalAccessException("Expected crash for testing");
    }

    @Override
    public boolean completeHandshake(ModularArtifConnection connection) throws Throwable {
        return false;
    }

    @Override
    public boolean hasHighHandshakePriority() {
        return false;
    }
}
