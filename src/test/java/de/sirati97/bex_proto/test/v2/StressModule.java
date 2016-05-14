package de.sirati97.bex_proto.test.v2;

import de.sirati97.bex_proto.datahandler.Type;
import de.sirati97.bex_proto.v2.IPacketDefinition;
import de.sirati97.bex_proto.v2.Packet;
import de.sirati97.bex_proto.v2.PacketDefinition;
import de.sirati97.bex_proto.v2.PacketHandler;
import de.sirati97.bex_proto.v2.ReceivedPacket;
import de.sirati97.bex_proto.v2.module.IModuleHandshake;
import de.sirati97.bex_proto.v2.module.ModularArtifConnection;
import de.sirati97.bex_proto.v2.module.Module;
import de.sirati97.bex_proto.v2.module.internal.ICallback;
import de.sirati97.bex_proto.v2.module.internal.YieldCause;

/**
 * Created by sirati97 on 13.04.2016.
 */
public class StressModule extends Module<StressModule.StressData> implements IModuleHandshake, PacketHandler {
    private static class StressPacketDefinition extends PacketDefinition {
        public StressPacketDefinition(short id, PacketHandler executor) {
            super(id, executor, Type.Integer);
        }
    }
    static class StressData {
        public ICallback callback;
        public boolean done;
        public int last = 0;
    }

    public StressModule() {
        super((short) 1);
    }

    @Override
    public void onHandshake(ModularArtifConnection connection, ICallback callback) {
        StressData data = getOrCreateModuleData(connection);
        data.callback = callback;
        data.last = 1;
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
    protected IPacketDefinition createPacket() {
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
        if (data.last+2!=i) {
            data.callback.error(new IllegalAccessException("wrong number received. stress test failed " + data.last + "=/=" + i));
        }
        data.last = i;
        if (i == Short.MAX_VALUE-1) {
            data.done = true;
        }
        if (i == Short.MAX_VALUE) {
            data.done = true;
            data.callback.callback();
            return;
        }
        data.callback.yield(YieldCause.PacketReceived);
        (new Packet(packet.getDefinition(),(i+1))).sendTo(packet.getSender());
    }




}
