package de.sirati97.bex_proto.test.v2;

import de.sirati97.bex_proto.v2.IPacketDefinition;
import de.sirati97.bex_proto.v2.PacketCollection;
import de.sirati97.bex_proto.v2.service.modular.IModuleHandshake;
import de.sirati97.bex_proto.v2.service.modular.ModularService;
import de.sirati97.bex_proto.v2.service.modular.internal.ICallback;
import de.sirati97.bex_proto.v2.service.modular.internal.InternalModule;

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
    public Object createData(ModularService connection) {
        return null;
    }

    @Override
    public void onHandshake(ModularService connection, final ICallback callback) {

    }

    @Override
    public void onHandshakeServerSide(ModularService connection, ICallback callback) throws Throwable {
        throw  new IllegalAccessException("Expected crash for testing");
    }

    @Override
    public boolean completeHandshake(ModularService connection) throws Throwable {
        return false;
    }

    @Override
    public boolean hasHighHandshakePriority() {
        return false;
    }
}
