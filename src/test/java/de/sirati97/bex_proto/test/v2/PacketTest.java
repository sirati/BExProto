package de.sirati97.bex_proto.test.v2;

import de.sirati97.bex_proto.datahandler.ArrayType;
import de.sirati97.bex_proto.datahandler.InetAddressPort;
import de.sirati97.bex_proto.datahandler.NullableType;
import de.sirati97.bex_proto.datahandler.Stream;
import de.sirati97.bex_proto.datahandler.Type;
import de.sirati97.bex_proto.util.CursorByteBuffer;
import de.sirati97.bex_proto.v2.Packet;
import de.sirati97.bex_proto.v2.PacketCollection;
import de.sirati97.bex_proto.v2.PacketDefinition;
import de.sirati97.bex_proto.v2.PacketExecutor;
import de.sirati97.bex_proto.v2.PacketManager;
import de.sirati97.bex_proto.v2.ReceivedPacket;
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
                    Type.Boolean,
                    Type.Byte, //numbers
                    Type.Short,
                    Type.Integer,
                    Type.Long,
                    Type.Float,
                    Type.Double,
                    Type.String_ISO_8859_1, //strings with different charsets
                    Type.String_US_ASCII,
                    Type.String_Utf_8,
                    Type.String_Utf_16,
                    Type.String_Utf_16BE,
                    Type.String_Utf_16LE,
                    Type.Type, //The Type itself (also sendable)
                    Type.UUID,
                    Type.InetAddress,
                    Type.InetAddressPort,
                    Type.JavaThrowable, //JavaTypes
                    new NullableType(Type.Boolean), //tests NullableType
                    new NullableType(Type.Boolean));

            Packet packetSend = new Packet(definition1, //Create packet and fill in values
                    true,
                    (byte)(Byte.MIN_VALUE/2),
                    (short)(Short.MAX_VALUE/2),
                    Integer.MIN_VALUE/2,
                    Long.MAX_VALUE/2,
                    Float.NaN,
                    Double.POSITIVE_INFINITY,
                    "Hello World",
                    "Hello World",
                    "Hello World",
                    "Hello World",
                    "Hello World",
                    "Hello World",
                    new NullableType(new ArrayType<>(new NullableType<>(Type.DynamicObj))),
                    UUID.randomUUID(),
                    InetAddress.getByAddress(new byte[]{127,0,0,1}),
                    new InetAddressPort(InetAddress.getByAddress(new byte[]{8,8,8,8}), 20),
                    new TestException("TEXT1"),
                    null, false);

            Stream stream = packetSend.createStream();
            CursorByteBuffer buffer = new CursorByteBuffer(stream.getByteBuffer().getBytes(), null);
            PacketDefinition extracted = (PacketDefinition) collection.getPacket(buffer);
            assertTrue("PacketDefinition have different length!", definition1==extracted);
            Packet packetReceived = PacketManager.extract(extracted, buffer);
            assertTrue("Arguments have different length!", packetSend.getArgumentLength() == packetReceived.getArgumentLength());
            for (int i=0;i<definition1.getArgumentLength();i++) {
                assertTrue("Received argument is different from send argument. index="+i,
                        (packetSend.get(i)==null&&packetReceived.get(i)==null) || packetSend.get(i).equals(packetReceived.get(i)));
            }
        } catch (AssertionError e) {
            throw e;
        } catch (Throwable e) {
            e.printStackTrace();
            throw new AssertionError("Unexpected exception: "+e.toString(), e);
        }
    }

    @Override
    public void execute(ReceivedPacket packet) {
        fail("Should not be called in this test. Packet is not executed");
    }
}
