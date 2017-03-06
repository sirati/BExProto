package de.sirati97.bex_proto.test.v2;

import de.sirati97.bex_proto.builder.Builder;
import de.sirati97.bex_proto.builder.IpPortAddress;
import de.sirati97.bex_proto.datahandler.Types;
import de.sirati97.bex_proto.events.EventHandler;
import de.sirati97.bex_proto.events.EventPriority;
import de.sirati97.bex_proto.events.GenericEventHandler;
import de.sirati97.bex_proto.events.Listener;
import de.sirati97.bex_proto.v2.PacketCollection;
import de.sirati97.bex_proto.v2.PacketDefinition;
import de.sirati97.bex_proto.v2.ReceivedPacket;
import de.sirati97.bex_proto.v2.SelfHandlingPacketDefinition;
import de.sirati97.bex_proto.v2.service.basic.BasicService;
import de.sirati97.bex_proto.v2.events.NewConnectionEvent;
import de.sirati97.bex_proto.v2.networkmodel.IServer;

import static de.sirati97.bex_proto.builder.ServiceTypes.ModularService;

/**
 * Created by sirati97 on 18.04.2016.
 */
public class EchoServer implements Listener {
    public static void main(String... args) throws Throwable {
        EchoServer server = new EchoServer();
        server.start();
    }

    private PacketCollection collection = new PacketCollection();
    private PacketDefinition packetWelcome = new SelfHandlingPacketDefinition((short)0, collection) {
        @Override
        public void execute(ReceivedPacket packet) {
            packet.sendTo(packet.getSender());
        }
    };
    private PacketDefinition packetMessage = new SelfHandlingPacketDefinition((short) 1, collection, Types.String_Utf_8) {
        @Override
        public void execute(ReceivedPacket packet) {
            System.out.println("Received from " + ((BasicService)packet.getSender()).getConnectionName() + ": " + packet.get(0));
            packet.sendTo(packet.getSender());
        }
    };

    public void start() throws Throwable {
        IServer server = new Builder<>(ModularService,  collection).buildServer(new IpPortAddress(12312)); //keine addresse >- *
        server.registerEventListener(this);
        server.startListening();
    }

    @GenericEventHandler(generics = {de.sirati97.bex_proto.v2.service.modular.ModularService.class})
    @EventHandler(priority = EventPriority.Monitor)
    public void onNewConnectionEvent(NewConnectionEvent<de.sirati97.bex_proto.v2.service.modular.ModularService> event) {
        System.out.println("Server accepted new connection");
    }
}
