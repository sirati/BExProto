package de.sirati97.bex_proto.test.v2;

import de.sirati97.bex_proto.datahandler.Types;
import de.sirati97.bex_proto.v2.IPacketDefinition;
import de.sirati97.bex_proto.v2.Packet;
import de.sirati97.bex_proto.v2.PacketDefinition;
import de.sirati97.bex_proto.v2.IPacketHandler;
import de.sirati97.bex_proto.v2.ReceivedPacket;
import de.sirati97.bex_proto.v2.service.modular.IModuleHandshake;
import de.sirati97.bex_proto.v2.service.modular.ModularService;
import de.sirati97.bex_proto.v2.service.modular.Module;
import de.sirati97.bex_proto.v2.service.modular.internal.ICallback;
import de.sirati97.bex_proto.v2.service.modular.internal.YieldCause;

/**
 * Created by sirati97 on 13.04.2016.
 */
public class FastStressModule extends Module<FastStressModule.StressData> implements IModuleHandshake, IPacketHandler {
    private static class StressPacketDefinition extends PacketDefinition {
        public StressPacketDefinition(short id, IPacketHandler executor) {
            super(id, executor, Types.Integer);
        }
    }
    static class StressData {
        public ICallback callback;
        public boolean done;
    }

    public FastStressModule() {
        super((short) 1);
    }

    @Override
    public void onHandshake(ModularService connection, ICallback callback) {
        getOrCreateModuleData(connection).callback = callback;
        (new Packet((PacketDefinition) getPacket(),2)).sendTo(connection);
    }

    @Override
    public void onHandshakeServerSide(ModularService connection, ICallback callback) throws Throwable {
        getOrCreateModuleData(connection).callback = callback;
    }

    @Override
    public boolean completeHandshake(ModularService connection) throws Throwable {
        return removeModuleData(connection).done;
    }

    @Override
    public boolean hasHighHandshakePriority() {
        return false;
    }

    @Override
    protected IPacketDefinition createPacket() {
        return new StressPacketDefinition(getId(), this);
    }

    @Override
    public StressData createData(ModularService connection) {
        return new StressData();
    }

    @Override
    public void execute(ReceivedPacket packet) {
        int i = packet.get(0);
        StressData data = getModuleData(packet.getSender());
        if (i == Short.MAX_VALUE-1) {
            data.done = true;
        }
        if (i == Short.MAX_VALUE) {
            data.done = true;
            data.callback.callback();
            return;
        }
        if (i%1000==0) {
            System.out.println(i);
        }
        data.callback.yield(YieldCause.PacketReceived);
        (new Packet(packet.getDefinition(),(i+1))).sendTo(packet.getSender());
    }




}
