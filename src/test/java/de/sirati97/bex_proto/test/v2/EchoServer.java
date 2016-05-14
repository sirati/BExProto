package de.sirati97.bex_proto.test.v2;

import de.sirati97.bex_proto.datahandler.Type;
import de.sirati97.bex_proto.events.EventHandler;
import de.sirati97.bex_proto.events.EventPriority;
import de.sirati97.bex_proto.events.GenericEventHandler;
import de.sirati97.bex_proto.events.Listener;
import de.sirati97.bex_proto.threading.ThreadPoolAsyncHelper;
import de.sirati97.bex_proto.util.logging.ILogger;
import de.sirati97.bex_proto.util.logging.SysOutLogger;
import de.sirati97.bex_proto.v2.PacketDefinition;
import de.sirati97.bex_proto.v2.PacketHandler;
import de.sirati97.bex_proto.v2.ReceivedPacket;
import de.sirati97.bex_proto.v2.ServerBase;
import de.sirati97.bex_proto.v2.events.NewConnectionEvent;
import de.sirati97.bex_proto.v2.io.tcp.TcpAIOServer;
import de.sirati97.bex_proto.v2.module.ModularArtifConnection;
import de.sirati97.bex_proto.v2.module.ModularArtifConnectionFactory;
import de.sirati97.bex_proto.v2.module.ModuleHandler;

/**
 * Created by sirati97 on 18.04.2016.
 */
public class EchoServer implements PacketHandler, Listener {

    public void start() throws Throwable {
        ILogger log = new SysOutLogger();
        ThreadPoolAsyncHelper helper = new ThreadPoolAsyncHelper();
        PacketDefinition definition = new PacketDefinition((short)0, this, Type.String_Utf_8); //this is the executor (packethandler)
        ModuleHandler moduleHandler = new ModuleHandler(definition, helper, log);
        ModularArtifConnectionFactory factory = new ModularArtifConnectionFactory(moduleHandler);
        ServerBase server = new TcpAIOServer<>(factory, 12312); //keine addresse >- *
        server.registerEventListener(this);
        server.startListening();
    }

    @Override
    public void execute(ReceivedPacket packet) {
        System.out.println("Received: " + packet.get(0));
        packet.sendTo(packet.getSender());
    }

    @GenericEventHandler(generics = {ModularArtifConnection.class})
    @EventHandler(priority = EventPriority.Monitor)
    public void onNewConnectionEvent(NewConnectionEvent<ModularArtifConnection> event) {
        System.out.println("Server accepted new connection");
    }
}
