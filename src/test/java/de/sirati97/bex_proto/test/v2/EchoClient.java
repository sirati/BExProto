package de.sirati97.bex_proto.test.v2;

import de.sirati97.bex_proto.builder.Builder;
import de.sirati97.bex_proto.builder.IpPortAddress;
import de.sirati97.bex_proto.datahandler.Types;
import de.sirati97.bex_proto.v2.Packet;
import de.sirati97.bex_proto.v2.PacketCollection;
import de.sirati97.bex_proto.v2.PacketDefinition;
import de.sirati97.bex_proto.v2.ReceivedPacket;
import de.sirati97.bex_proto.v2.SelfHandlingPacketDefinition;
import de.sirati97.bex_proto.v2.networkmodell.IClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;

import static de.sirati97.bex_proto.builder.ServiceTypes.ModularService;

/**
 * Created by sirati97 on 18.04.2016.
 */
public class EchoClient {
    public static void main(String... args) throws Throwable {
        EchoClient client = new EchoClient();
        client.start();
    }

    private PacketCollection collection = new PacketCollection();
    private PacketDefinition packetWelcome = new SelfHandlingPacketDefinition((short)0, collection) {
        @Override
        public void execute(ReceivedPacket packet) {
            System.out.println("Server welcomed client");
        }
    };
    private PacketDefinition packetMessage = new SelfHandlingPacketDefinition((short) 1, collection, Types.String_Utf_8) {
        @Override
        public void execute(ReceivedPacket packet) {
            System.out.println("Received message from server: " + packet.get(0));
        }
    };

    public void start() throws Throwable {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Enter client name: ");
        String name = in.readLine();
        IClient client = new Builder<>(ModularService,  collection).buildClient(new IpPortAddress(InetAddress.getLocalHost(), 12312), name);
        client.connect();
        Packet packetWelcome = new Packet(this.packetWelcome);
        packetWelcome.sendTo(client.getConnection());
        String input;
        while (true) {
            System.out.print("Input: ");
            input = in.readLine();
            if ("exit".equalsIgnoreCase(input)) {
                break;
            }
            Packet packet = new Packet(packetMessage, input);
            packet.sendTo(client.getConnection());
        }
        client.stop();
    }

}
