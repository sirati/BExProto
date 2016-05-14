package de.sirati97.bex_proto.test.v2;

import de.sirati97.bex_proto.v2.IPacketDefinition;
import de.sirati97.bex_proto.v2.PacketCollection;
import de.sirati97.bex_proto.v2.module.IModuleHandshake;
import de.sirati97.bex_proto.v2.module.ModularArtifConnection;
import de.sirati97.bex_proto.v2.module.internal.ICallback;
import de.sirati97.bex_proto.v2.module.internal.InternalModule;

/**
 * Created by sirati97 on 13.04.2016.
 */
public class FailModule2 extends InternalModule implements IModuleHandshake{
    public FailModule2() {
        super((short) -99);
    }

    @Override
    protected IPacketDefinition createPacket() {
        return new PacketCollection();
    }

    @Override
    public Object createData(ModularArtifConnection connection) {
        return null;
    }

    @Override
    public void onHandshake(ModularArtifConnection connection, final ICallback callback) {

    }

    @Override
    public void onHandshakeServerSide(ModularArtifConnection connection, ICallback callback) throws Throwable {
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
