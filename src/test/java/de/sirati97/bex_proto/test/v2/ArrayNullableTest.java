package de.sirati97.bex_proto.test.v2;

import de.sirati97.bex_proto.datahandler.Type;
import de.sirati97.bex_proto.util.CursorByteBuffer;
import de.sirati97.bex_proto.util.bytebuffer.ByteBuffer;
import de.sirati97.bex_proto.v2.Packet;
import de.sirati97.bex_proto.v2.PacketDefinition;
import de.sirati97.bex_proto.v2.PacketHandler;
import de.sirati97.bex_proto.v2.PacketManager;
import de.sirati97.bex_proto.v2.ReceivedPacket;
import de.sirati97.bex_proto.v2.io.TestIOHandler;
import org.junit.Assert;
import org.junit.Test;

import java.util.Random;

/**
 * Created by sirati97 on 28.04.2016.
 */
public class ArrayNullableTest implements PacketHandler {


    @Test
    public void start() {
        PacketDefinition definition = new PacketDefinition((short)0, this, Type.Long.asNullable().asArray());
        Long[] data = new Long[256];
        Random rnd = new Random();
        for (int i = 0; i < data.length; i++) {
            if(rnd.nextBoolean()) {
                data[i] = rnd.nextLong();
            }
        }

        Packet packetSend = new Packet(definition, (Object)data);

        ByteBuffer stream = packetSend.createStream();
        byte[] bytes = stream.getBytes();

        System.out.println(TestIOHandler.bytesToString(bytes));

        CursorByteBuffer buffer = new CursorByteBuffer(bytes, null);

        Packet packetReceived = PacketManager.extract(definition, buffer);
        Long[] dataReceived = packetReceived.get(0);

        Assert.assertArrayEquals(data, dataReceived);
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
