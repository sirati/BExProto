package de.sirati97.bex_proto.test.v2.packettest;

import de.sirati97.bex_proto.datahandler.Type;
import de.sirati97.bex_proto.threading.AdvThreadAsyncHelper;
import de.sirati97.bex_proto.threading.AsyncHelper;
import de.sirati97.bex_proto.util.logging.ILogger;
import de.sirati97.bex_proto.util.logging.SysOutLogger;
import de.sirati97.bex_proto.v2.Packet;
import de.sirati97.bex_proto.v2.PacketDefinition;
import de.sirati97.bex_proto.v2.PacketExecutor;
import de.sirati97.bex_proto.v2.ReceivedPacket;
import de.sirati97.bex_proto.v2.artifcon.TestIOHandler;
import de.sirati97.bex_proto.v2.module.HandshakeException;
import de.sirati97.bex_proto.v2.module.ModularArtifConnection;
import de.sirati97.bex_proto.v2.module.ModuleHandler;
import de.sirati97.bex_proto.v2.module.internal.BouncyCastleHelper;
import org.junit.Test;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeoutException;

/**
 * Created by sirati97 on 13.04.2016.
 */
public class HandshakeTest implements PacketExecutor{

    @Test
    public void start() throws TimeoutException, InterruptedException, HandshakeException, NoSuchAlgorithmException {
        ILogger log = new SysOutLogger();
        Long timestamp;
        try {
            log.info("Handshake test preparing");
            PacketDefinition definition = new PacketDefinition((short)0, this, Type.String_Utf_8);
            AsyncHelper helper = new AdvThreadAsyncHelper(6);
            ModuleHandler moduleHandler = new ModuleHandler(definition, helper, log);
            //moduleHandler.register(new FailModule()); // - will fail every handshake
            //moduleHandler.register(new FailModule2()); // - will fail every handshake
            //moduleHandler.register(new StressModule()); //will send 2^15 packets
            TestIOHandler pipe1 = new TestIOHandler();
            TestIOHandler pipe2 = new TestIOHandler();
            ModularArtifConnection connection1 = new ModularArtifConnection("TestCon1", pipe1, moduleHandler);
            ModularArtifConnection connection2 = new ModularArtifConnection("TestCon2", pipe2, moduleHandler);
            MessageDigest md = MessageDigest.getInstance(BouncyCastleHelper.HASH_ALGORITHM);
            connection1.setHashAlgorithm(md);
            connection2.setHashAlgorithm(md);
            pipe1.receiver = connection2;
            pipe2.receiver = connection1;
            log.info("Handshake test start");
            timestamp = System.nanoTime();
            connection1.connect();
            timestamp = System.nanoTime()-timestamp;
            Packet p = new Packet(definition, "Testing...");
            p.sendTo(connection1);
        } catch (Throwable e) {
            e.printStackTrace();
            throw e;

        }
        log.info("Handshake test successful. Handshake took " + (timestamp/1000000) + "ms");
    }

    @Override
    public void execute(ReceivedPacket packet) {
        System.out.println(packet.get(0));
    }
}
