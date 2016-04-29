package de.sirati97.bex_proto.test.v2;

import de.sirati97.bex_proto.datahandler.IArrayType;
import de.sirati97.bex_proto.datahandler.Stream;
import de.sirati97.bex_proto.datahandler.Type;
import de.sirati97.bex_proto.util.CursorByteBuffer;
import de.sirati97.bex_proto.v2.Packet;
import de.sirati97.bex_proto.v2.PacketDefinition;
import de.sirati97.bex_proto.v2.PacketExecutor;
import de.sirati97.bex_proto.v2.PacketManager;
import de.sirati97.bex_proto.v2.ReceivedPacket;
import de.sirati97.bex_proto.v2.io.TestIOHandler;
import org.junit.Assert;
import org.junit.Test;

import java.util.Random;

/**
 * Created by sirati97 on 28.04.2016.
 */
public class BitArrayTest implements PacketExecutor{


    @Test
    public void start() {
        IArrayType<Boolean> bitArrayType = Type.Boolean.asArray();
        PacketDefinition definition = new PacketDefinition((short)0, this, bitArrayType);
        boolean[] bits = new boolean[256];
        Random rnd = new Random();
        for (int i = 0; i < bits.length; i++) {
            bits[i] = rnd.nextBoolean();
        }

        Packet packetSend = new Packet(definition, bits);

        Stream stream = packetSend.createStream();
        byte[] bytes = stream.getByteBuffer().getBytes();

        System.out.println(TestIOHandler.bytesToString(bytes));

        CursorByteBuffer buffer = new CursorByteBuffer(bytes, null);

        Packet packetReceived = PacketManager.extract(definition, buffer);
        boolean[] bitsReceived = packetReceived.get(0);
        System.out.println("Bits send:     " + bitsToString(bits));
        System.out.println("Bits received: " + bitsToString(bitsReceived));

        Assert.assertArrayEquals("Bits are not equal", bits, bitsReceived);
    }

    @Override
    public void execute(ReceivedPacket packet) {

    }


    public static String bitsToString(boolean[] stream) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < stream.length; i++) {
            sb.append(stream[i]?'1':'0');
            if (i > 0) {
                if (i % 8 == 7) {
                    sb.append(' ');
                } else if (i % 4 == 3) {
                    sb.append('_');
                }
            }
        }
        return sb.toString();
    }

}
