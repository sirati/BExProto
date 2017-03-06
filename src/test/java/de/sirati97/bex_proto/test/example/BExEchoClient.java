package de.sirati97.bex_proto.test.example;

import de.sirati97.bex_proto.builder.BExBuilder;
import de.sirati97.bex_proto.builder.IpPortAddress;
import de.sirati97.bex_proto.datahandler.Types;
import de.sirati97.bex_proto.v2.Packet;
import de.sirati97.bex_proto.v2.PacketDefinition;
import de.sirati97.bex_proto.v2.ReceivedPacket;
import de.sirati97.bex_proto.v2.SelfHandlingPacketDefinition;
import de.sirati97.bex_proto.v2.networkmodel.IClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;

import static de.sirati97.bex_proto.builder.ServiceTypes.BasicService;

/**
 * Created by sirati97 on 18.04.2016.
 */
public class BExEchoClient {
    public static void main(String... args) throws Throwable {
        BExEchoClient client = new BExEchoClient();
        client.start();
    }

    private PacketDefinition packetMessage = new SelfHandlingPacketDefinition((short) 0, Types.String_Utf_8) {
        @Override
        public void execute(ReceivedPacket packet) {
            System.out.println("Received: " + packet.get(0));
        }
    };

    public void start() throws Throwable {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        BExBuilder builder = new BExBuilder<>(BasicService,  packetMessage);
        IClient client = builder.buildClient(new IpPortAddress(InetAddress.getLocalHost(), 12312), "EchoClient");
        client.connect();
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
