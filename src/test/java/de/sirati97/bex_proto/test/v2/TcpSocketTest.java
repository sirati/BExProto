package de.sirati97.bex_proto.test.v2;

import de.sirati97.bex_proto.datahandler.Type;
import de.sirati97.bex_proto.events.EventHandler;
import de.sirati97.bex_proto.events.EventPriority;
import de.sirati97.bex_proto.events.GenericEventHandler;
import de.sirati97.bex_proto.events.Listener;
import de.sirati97.bex_proto.threading.AdvThreadAsyncHelper;
import de.sirati97.bex_proto.util.logging.ILogger;
import de.sirati97.bex_proto.util.logging.SysOutLogger;
import de.sirati97.bex_proto.v2.Packet;
import de.sirati97.bex_proto.v2.PacketDefinition;
import de.sirati97.bex_proto.v2.PacketExecutor;
import de.sirati97.bex_proto.v2.ReceivedPacket;
import de.sirati97.bex_proto.v2.events.NewConnectionEvent;
import de.sirati97.bex_proto.v2.io.tcp.TcpClient;
import de.sirati97.bex_proto.v2.io.tcp.TcpServer;
import de.sirati97.bex_proto.v2.module.ModularArtifConnection;
import de.sirati97.bex_proto.v2.module.ModularArtifConnectionFactory;
import de.sirati97.bex_proto.v2.module.ModuleHandler;
import org.junit.Test;

import java.net.InetAddress;
import java.util.Set;

import static org.junit.Assert.fail;

/**
 * Created by sirati97 on 18.04.2016.
 */
public class TcpSocketTest implements PacketExecutor, Listener {
    private final Object receiveMutex = new Object();
    private boolean received = false;

    @Test
    public void start() throws Throwable {
        ILogger log = new SysOutLogger();
        Long timestamp;
        AdvThreadAsyncHelper helper = new AdvThreadAsyncHelper();
        try {
            try {
                PacketDefinition definition = new PacketDefinition((short)0, this, Type.String_Utf_8);
                ModuleHandler moduleHandler = new ModuleHandler(definition, helper, log);
                ModularArtifConnectionFactory factory = new ModularArtifConnectionFactory(moduleHandler);
                InetAddress address = InetAddress.getByName("localhost");
                TcpServer server = new TcpServer<>(factory, address, 12312);
                server.registerEventListener(this);
                TcpClient client = new TcpClient<>(factory, "TestConnection", address, 12312, address, 12313);
                server.startListening();
                timestamp = System.nanoTime();
                client.connect();
                timestamp = System.nanoTime()-timestamp;
                Packet p = new Packet(definition, "Tcp connection testing...");
                p.sendTo(client.getConnection());


                synchronized (receiveMutex) {
                    if (!received) {
                        receiveMutex.wait(2000);
                        if (!received) {
                            fail("Test Packet was not received after 2s");
                        }
                    }
                }

                server.stop();
                client.stop();
                Thread.sleep(100);
            } catch (Throwable e) {
                e.printStackTrace();
                throw e;
            }
            log.info("Handshake over tcp took " + (timestamp/1000000) + "ms");


            Set<Thread> threadSet = helper.getActiveThreads();
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
                fail("There are still thread(s) active: " + threadSet.size());
            }
            log.info("Tcp socket test successful.");
        } finally {
            helper.stop();
        }
    }

    @Override
    public void execute(ReceivedPacket packet) {
        synchronized (receiveMutex) {
            received = true;
            receiveMutex.notifyAll();
        }
        System.out.println(packet.get(0));
    }

    @GenericEventHandler(generics = {ModularArtifConnection.class})
    @EventHandler(priority = EventPriority.Monitor)
    public void onNewConnectionEvent(NewConnectionEvent<ModularArtifConnection> event) {
        System.out.println("Server accepted new connection");
    }
}
