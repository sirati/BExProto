package de.sirati97.bex_proto.test.v2.packettest;

import de.sirati97.bex_proto.threading.AdvThreadAsyncHelper;
import de.sirati97.bex_proto.threading.AsyncHelper;
import de.sirati97.bex_proto.util.logging.ILogger;
import de.sirati97.bex_proto.util.logging.SysOutLogger;
import de.sirati97.bex_proto.v2.PacketCollection;
import de.sirati97.bex_proto.v2.module.HandshakeErroredException;
import de.sirati97.bex_proto.v2.module.ModularArtifConnection;
import de.sirati97.bex_proto.v2.module.ModuleHandler;
import org.junit.Test;

import java.util.concurrent.TimeoutException;

/**
 * Created by sirati97 on 13.04.2016.
 */
public class HandshakeTest {

    @Test
    public void start() throws TimeoutException, InterruptedException, HandshakeErroredException {
        ILogger log = new SysOutLogger();
        Long timestamp;
        try {
            log.info("Handshake test preparing");
            ModuleHandler moduleHandler = new ModuleHandler(new PacketCollection(), log);
            //moduleHandler.register(new FailModule()); // - will fail every handshake
            //moduleHandler.register(new StressModule()); will take over 90 seconds
            AsyncHelper helper = new AdvThreadAsyncHelper(4);
            TestIO pipe1 = new TestIO();
            TestIO pipe2 = new TestIO();
            ModularArtifConnection connection1 = new ModularArtifConnection("TestCon1", helper, pipe1, moduleHandler);
            ModularArtifConnection connection2 = new ModularArtifConnection("TestCon2", helper, pipe2, moduleHandler);
            pipe1.receiver = connection2;
            pipe2.receiver = connection1;
            log.info("Handshake test start");
            timestamp = System.nanoTime();
            connection1.connect();
            timestamp = System.nanoTime()-timestamp;

        } catch (Throwable e) {
            e.printStackTrace();
            throw e;

        }
        log.info("Handshake test successful. Handshake took " + (timestamp/1000000) + "ms");
    }
}
