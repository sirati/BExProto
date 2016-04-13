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
import de.sirati97.bex_proto.v2.module.internal.IHandshakeCallback;

/**
 * Created by sirati97 on 13.04.2016.
 */
public class StressModule extends Module<StressModule.StressData> implements IModuleHandshake, PacketExecutor {
    private static class StressPacketDefinition extends PacketDefinition {
        public StressPacketDefinition(short id, PacketExecutor executor) {
            super(id, executor, Type.Integer);
        }
    }
    static class StressData {
        public IHandshakeCallback callback;
    }

    public StressModule() {
        super((short) 1);
    }

    @Override
    public void onHandshake(ModularArtifConnection connection, IHandshakeCallback callback) {
        getOrCreateModuleData(connection).callback = callback;
        (new Packet((PacketDefinition) getPacket(),2)).sendTo(connection);
    }

    @Override
    public boolean hasHighHandshakePriority() {
        return false;
    }

    @Override
    protected IPacket createPacket() {
        System.out.println(getId());
        return new StressPacketDefinition(getId(), this);
    }

    @Override
    public StressData createData(ModularArtifConnection connection) {
        return new StressData();
    }

    @Override
    public void execute(ReceivedPacket packet) {
        int i = packet.get(0);
        if (i == Short.MAX_VALUE*2000+1) {
            removeModuleData(packet.getSender()).callback.callback();
            return;
        }
        if (i%2==1) {
            getModuleData(packet.getSender()).callback.yield();
        }
        (new Packet(packet.getDefinition(),(i+1))).sendTo(packet.getSender());
    }




}
