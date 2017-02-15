package de.sirati97.bex_proto.test.example;

import de.sirati97.bex_proto.builder.Builder;
import de.sirati97.bex_proto.builder.IpPortAddress;
import de.sirati97.bex_proto.builder.ServiceTypes;
import de.sirati97.bex_proto.datahandler.Type;
import de.sirati97.bex_proto.events.EventHandler;
import de.sirati97.bex_proto.events.EventPriority;
import de.sirati97.bex_proto.events.GenericEventHandler;
import de.sirati97.bex_proto.events.Listener;
import de.sirati97.bex_proto.threading.AsyncHelper;
import de.sirati97.bex_proto.threading.ShutdownBehavior;
import de.sirati97.bex_proto.threading.ThreadPoolAsyncHelper;
import de.sirati97.bex_proto.v2.PacketDefinition;
import de.sirati97.bex_proto.v2.ReceivedPacket;
import de.sirati97.bex_proto.v2.SelfHandlingPacketDefinition;
import de.sirati97.bex_proto.v2.events.NewConnectionEvent;
import de.sirati97.bex_proto.v2.networkmodell.IServer;
import de.sirati97.bex_proto.v2.service.basic.BasicService;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import static de.sirati97.bex_proto.v2.networkmodell.CommonNetworkStackImplementation.AsynchronousIO;

/**
 * Created by sirati97 on 18.04.2016.
 */
public class BExEchoServer implements Listener {
    public static void main(String... args) throws Throwable {
        BExEchoServer server = new BExEchoServer();
        server.start();
    }

    private PacketDefinition packetMessage = new SelfHandlingPacketDefinition((short) 1, Type.String_Utf_8) {
        @Override
        public void execute(ReceivedPacket packet) {
            System.out.println("Received: " + packet.get(0));
            packet.sendTo(packet.getSender());
        }
    };

    public void start() throws Throwable {
        AsyncHelper helper = new ThreadPoolAsyncHelper(ShutdownBehavior.JavaVMShutdownNow);
        IServer server = new Builder<>(ServiceTypes.BasicService,  packetMessage).asyncHelper(helper).stackImplementation(AsynchronousIO).buildServer(new IpPortAddress(12312)); //keine addresse -> 0.0.0.0
        server.registerEventListener(this);
        server.startListening();

        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String input;
        while (true) {
            input = in.readLine();
            if ("exit".equalsIgnoreCase(input)) {
                break;
            } else if ("gc".equalsIgnoreCase(input)) {
                System.gc();
            } else {
                System.out.println("Unknown command");
            }
        }

        server.stopListening();
    }

    @GenericEventHandler(generics = {BasicService.class})
    @EventHandler(priority = EventPriority.Monitor)
    public void onNewConnectionEvent(NewConnectionEvent<BasicService> event) {
        System.out.println("Server accepted new connection");
    }
}
