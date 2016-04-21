package de.sirati97.bex_proto.test.v2.packettest;

import de.sirati97.bex_proto.datahandler.Type;
import de.sirati97.bex_proto.threading.AdvThreadAsyncHelper;
import de.sirati97.bex_proto.util.RuntimeGeneratedUnsecureEncryptionContainer;
import de.sirati97.bex_proto.util.logging.ILogger;
import de.sirati97.bex_proto.util.logging.SysOutLogger;
import de.sirati97.bex_proto.v2.Packet;
import de.sirati97.bex_proto.v2.PacketDefinition;
import de.sirati97.bex_proto.v2.PacketExecutor;
import de.sirati97.bex_proto.v2.ReceivedPacket;
import de.sirati97.bex_proto.v2.io.TestIOHandler;
import de.sirati97.bex_proto.v2.module.ModularArtifConnection;
import de.sirati97.bex_proto.v2.module.ModuleHandler;
import de.sirati97.bex_proto.v2.module.internal.EncryptionModule;
import org.junit.Test;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Set;

import static org.junit.Assert.fail;

/**
 * Created by sirati97 on 13.04.2016.
 */
public class EncryptedHandshakeTest implements PacketExecutor{
    private ILogger log = new SysOutLogger();
    private AdvThreadAsyncHelper asyncHelper = new AdvThreadAsyncHelper(6);
    private final Object receiveMutex = new Object();
    private boolean received = false;

    @Test
    public void start() throws Throwable {
        Long timestamp;
        try {
            try {
                log.info("Encrypted Handshake test preparing");
                PacketDefinition definition1 = new PacketDefinition((short) 0, this, Type.String_Utf_8);
                PacketDefinition definition2 = definition1.clone();
                TestIOHandler pipe1 = new TestIOHandler();
                TestIOHandler pipe2 = new TestIOHandler();
                ModularArtifConnection connection1 = createConnection("TestEnCon1", definition1, pipe1);
                ModularArtifConnection connection2 = createConnection("TestEnCon2", definition2, pipe2);

                pipe1.receiver = pipe2;
                pipe2.receiver = pipe1;

                log.info("Encrypted Handshake test start");
                connection2.expectConnection();
                timestamp = System.nanoTime();
                connection1.connect();
                timestamp = System.nanoTime()-timestamp;
                Packet p = new Packet(definition1, "Encrypted Testing...");
                p.sendTo(connection1);
            } catch (Throwable e) {
                e.printStackTrace();
                throw e;

            }

            synchronized (receiveMutex) {
                if (!received) {
                    receiveMutex.wait(2000);
                    if (!received) {
                        fail("Test Packet was not received after 2s");
                    }
                }
            }

            Set<Thread> threadSet = asyncHelper.getActiveThreads();
            for (int i=0;i<100;i++) {
                if (threadSet.size() > 0) {
                    Thread.sleep(1);
                } else {
                    break;
                }
            }
            if (threadSet.size() > 0) {
                log.info("Still active threads: ");
                for (Thread t:threadSet) {
                    log.info(t.getName() + " state=" + t.getState().toString());
                }
                asyncHelper.stop();
                fail("There are still thread(s) active: " + threadSet.size());
            }
            asyncHelper.stop();
            log.info("Encrypted Handshake test successful. Handshake took " + (timestamp/1000000) + "ms");
        } finally {
            asyncHelper.stop();
        }
    }

    private ModularArtifConnection createConnection(String name, PacketDefinition definition, TestIOHandler pipe) throws NoSuchAlgorithmException, NoSuchProviderException {
        ModuleHandler handler = new ModuleHandler(definition, asyncHelper, log);
        handler.register(new EncryptionModule(new RuntimeGeneratedUnsecureEncryptionContainer()));
        //handler.register(new StressModule());
        return new ModularArtifConnection(name, pipe, handler);
    }

    @Override
    public void execute(ReceivedPacket packet) {
        synchronized (receiveMutex) {
            received = true;
            receiveMutex.notifyAll();
        }
        System.out.println(packet.get(0));
    }
}
