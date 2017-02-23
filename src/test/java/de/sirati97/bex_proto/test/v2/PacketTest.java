package de.sirati97.bex_proto.test.v2;

import de.sirati97.bex_proto.datahandler.InetAddressPort;
import de.sirati97.bex_proto.datahandler.Types;
import de.sirati97.bex_proto.util.CursorByteBuffer;
import de.sirati97.bex_proto.util.bytebuffer.ByteBuffer;
import de.sirati97.bex_proto.v2.Packet;
import de.sirati97.bex_proto.v2.PacketCollection;
import de.sirati97.bex_proto.v2.PacketDefinition;
import de.sirati97.bex_proto.v2.IPacketHandler;
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
public class PacketTest implements IPacketHandler {

    @Test
    public void start() {
        try {
            PacketCollection collection = new PacketCollection(this);
            PacketDefinition definition1 = new PacketDefinition((short)0, collection,
                    Types.Boolean,
                    Types.Byte, //numbers
                    Types.Short,
                    Types.Integer,
                    Types.Long,
                    Types.Float,
                    Types.Double,
                    Types.String_ISO_8859_1, //strings with different charsets
                    Types.String_US_ASCII,
                    Types.String_Utf_8,
                    Types.String_Utf_16,
                    Types.String_Utf_16BE,
                    Types.String_Utf_16LE,
                    Types.Type, //The Type itself (also sendable)
                    Types.UUID,
                    Types.InetAddress,
                    Types.InetAddressPort,
                    Types.JavaThrowable, //JavaTypes
                    Types.Boolean.asNullable(), //tests NullableType
                    Types.Boolean.asNullable());

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
                    Types.DynamicObj.asNullable().asArray().asNullable(),
                    UUID.randomUUID(),
                    InetAddress.getByAddress(new byte[]{127,0,0,1}),
                    new InetAddressPort(InetAddress.getByAddress(new byte[]{8,8,8,8}), 20),
                    new TestException("TEXT1"),
                    null, false);

            ByteBuffer stream = packetSend.createStream();
            CursorByteBuffer buffer = new CursorByteBuffer(stream.getBytes(), null);
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
