package de.sirati97.bex_proto.test.v2.packettest;

import de.sirati97.bex_proto.datahandler.ArrayType;
import de.sirati97.bex_proto.datahandler.InetAddressPort;
import de.sirati97.bex_proto.datahandler.NullableType;
import de.sirati97.bex_proto.datahandler.Stream;
import de.sirati97.bex_proto.datahandler.Type;
import de.sirati97.bex_proto.util.ByteBuffer;
import de.sirati97.bex_proto.v2.Packet;
import de.sirati97.bex_proto.v2.PacketCollection;
import de.sirati97.bex_proto.v2.PacketDefinition;
import de.sirati97.bex_proto.v2.PacketExecutor;
import de.sirati97.bex_proto.v2.PacketManager;
import org.junit.Test;

import java.net.InetAddress;
import java.util.UUID;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Created by sirati97 on 15.03.2016.
 */
public class PacketTest implements PacketExecutor{

    @Test
    public void start() {
        try {
            PacketCollection collection = new PacketCollection(this);
            PacketDefinition definition1 = new PacketDefinition((short)0, collection,
                    Type.Boolean, Type.Byte, Type.Short, Type.Integer, Type.Long, Type.Float, Type.Double,
                    Type.String_ISO_8859_1, Type.String_US_ASCII, Type.String_Utf_8, Type.String_Utf_16, Type.String_Utf_16BE, Type.String_Utf_16LE,
                    Type.Type, Type.UUID, Type.InetAddress, Type.InetAddressPort,
                    new NullableType(Type.Boolean),new NullableType(Type.Boolean));
            Packet packetSend = new Packet(definition1,
                    true, (byte)(Byte.MIN_VALUE/2), (short)(Short.MAX_VALUE/2), Integer.MIN_VALUE/2, Long.MAX_VALUE/2, Float.NaN, Double.POSITIVE_INFINITY,
                    "Hello World", "Hello World", "Hello World", "Hello World", "Hello World", "Hello World",
                    new NullableType(new ArrayType(new NullableType(Type.DynamicObj))), UUID.randomUUID(), InetAddress.getByAddress(new byte[]{127,0,0,1}), new InetAddressPort(InetAddress.getByName("google.de"), 20),
                    null, false);
            Stream stream = packetSend.createStream(null);
            ByteBuffer buffer = new ByteBuffer(stream.getBytes(), null);
            PacketDefinition extracted = (PacketDefinition) collection.getPacket(buffer);
            assertTrue("PacketDefinition differ!", definition1==extracted);
            Packet packetReceived = PacketManager.extract(extracted, buffer);
            assertTrue("Arguments length differ!", packetSend.getArgumentLength() == packetReceived.getArgumentLength());
            for (int i=0;i<definition1.getArgumentLength();i++) {
                assertTrue("Received argument differ from send argument. index="+i, (packetSend.get(i)==null&&packetReceived.get(i)==packetSend.get(i)) || packetSend.get(i).equals(packetReceived.get(i)));
            }
        } catch (AssertionError e) {
            throw e;
        } catch (Throwable e) {
            e.printStackTrace();
            throw new AssertionError("Unexpected exception: "+e.toString(), e);
        }


    }

    @Override
    public void execute(Packet packet) {
        fail("Should not be called in this test. Packet is not executed");
    }
}
