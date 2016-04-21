package de.sirati97.bex_proto.test.v2.packettest;

import de.sirati97.bex_proto.datahandler.Type;
import de.sirati97.bex_proto.v2.IPacket;
import de.sirati97.bex_proto.v2.Packet;
import de.sirati97.bex_proto.v2.PacketDefinition;
import de.sirati97.bex_proto.v2.PacketExecutor;
import de.sirati97.bex_proto.v2.ReceivedPacket;
import de.sirati97.bex_proto.v2.module.IModuleHandshake;
import de.sirati97.bex_proto.v2.module.ModularArtifConnection;
import de.sirati97.bex_proto.v2.module.Module;
import de.sirati97.bex_proto.v2.module.internal.ICallback;
import de.sirati97.bex_proto.v2.module.internal.YieldCause;

/**
 * Created by sirati97 on 13.04.2016.
 */
public class FastStressModule extends Module<FastStressModule.StressData> implements IModuleHandshake, PacketExecutor {
    private static class StressPacketDefinition extends PacketDefinition {
        public StressPacketDefinition(short id, PacketExecutor executor) {
            super(id, executor, Type.Integer);
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
    public void onHandshake(ModularArtifConnection connection, ICallback callback) {
        getOrCreateModuleData(connection).callback = callback;
        (new Packet((PacketDefinition) getPacket(),2)).sendTo(connection);
    }

    @Override
    public void onHandshakeServerSide(ModularArtifConnection connection, ICallback callback) throws Throwable {
        getOrCreateModuleData(connection).callback = callback;
    }

    @Override
    public boolean completeHandshake(ModularArtifConnection connection) throws Throwable {
        return removeModuleData(connection).done;
    }

    @Override
    public boolean hasHighHandshakePriority() {
        return false;
    }

    @Override
    protected IPacket createPacket() {
        return new StressPacketDefinition(getId(), this);
    }

    @Override
    public StressData createData(ModularArtifConnection connection) {
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
