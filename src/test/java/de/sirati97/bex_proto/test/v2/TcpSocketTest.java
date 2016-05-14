package de.sirati97.bex_proto.test.v2;

import de.sirati97.bex_proto.datahandler.Type;
import de.sirati97.bex_proto.events.EventHandler;
import de.sirati97.bex_proto.events.EventPriority;
import de.sirati97.bex_proto.events.GenericEventHandler;
import de.sirati97.bex_proto.events.Listener;
import de.sirati97.bex_proto.threading.AsyncTask;
import de.sirati97.bex_proto.threading.ThreadPoolAsyncHelper;
import de.sirati97.bex_proto.util.logging.ILogger;
import de.sirati97.bex_proto.util.logging.SysOutLogger;
import de.sirati97.bex_proto.v2.ClientBase;
import de.sirati97.bex_proto.v2.Packet;
import de.sirati97.bex_proto.v2.PacketDefinition;
import de.sirati97.bex_proto.v2.PacketHandler;
import de.sirati97.bex_proto.v2.ReceivedPacket;
import de.sirati97.bex_proto.v2.ServerBase;
import de.sirati97.bex_proto.v2.events.NewConnectionEvent;
import de.sirati97.bex_proto.v2.io.tcp.TcpAIOClient;
import de.sirati97.bex_proto.v2.io.tcp.TcpAIOServer;
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
public class TcpSocketTest implements PacketHandler, Listener {
    private final Object receiveMutex = new Object();
    private boolean received = false;

    @Test
    public void start() throws Throwable {
        ILogger log = new SysOutLogger();
        long timestamp;
        ThreadPoolAsyncHelper helper = new ThreadPoolAsyncHelper();
        try {
            try {
                PacketDefinition definition = new PacketDefinition((short)0, this, Type.String_Utf_8);
                ModuleHandler moduleHandler = new ModuleHandler(definition, helper, log);
                ModularArtifConnectionFactory factory = new ModularArtifConnectionFactory(moduleHandler);
                InetAddress address = InetAddress.getLocalHost();
                ServerBase server = new TcpAIOServer<>(factory, address, 12312);
                server.registerEventListener(this);
                ClientBase client = new TcpAIOClient<>(factory, "TestConnection", address, 12312);
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
            } catch (Throwable e) {
                e.printStackTrace();
                throw e;
            }
            log.info("Handshake over tcp took " + (timestamp/1000000) + "ms");


            Set<? extends AsyncTask> taskSet = helper.getActiveTasks();
            for (int i=0;i<100;i++) {
                if (taskSet.size() > 0) {
                    Thread.sleep(1);
                } else {
                    break;
                }
            }
            if (taskSet.size() > 0) {
                log.info("Still active threads: ");
                for (AsyncTask task:taskSet) {
                    Thread t = task.getThread();
                    log.info(t.getName() + " state=" + t.getState().toString());
                }
                fail("There are still thread(s) active: " + taskSet.size());
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
