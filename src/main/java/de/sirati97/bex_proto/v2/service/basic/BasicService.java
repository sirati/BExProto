package de.sirati97.bex_proto.v2.service.basic;

import de.sirati97.bex_proto.datahandler.BExStatic;
import de.sirati97.bex_proto.events.Event;
import de.sirati97.bex_proto.events.EventRegister;
import de.sirati97.bex_proto.events.IEventRegister;
import de.sirati97.bex_proto.events.Listener;
import de.sirati97.bex_proto.threading.IAsyncHelper;
import de.sirati97.bex_proto.util.IConnection;
import de.sirati97.bex_proto.util.bytebuffer.ByteBuffer;
import de.sirati97.bex_proto.util.logging.ILogger;
import de.sirati97.bex_proto.v2.IPacketDefinition;
import de.sirati97.bex_proto.v2.StreamChannel;
import de.sirati97.bex_proto.v2.StreamReader;
import de.sirati97.bex_proto.v2.events.ConnectionClosedEvent;
import de.sirati97.bex_proto.v2.io.IOHandler;
import org.apache.commons.lang3.Validate;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by sirati97 on 15.03.2016.
 */
public class BasicService implements IConnection, IEventRegister {
    private final StreamModifiers streamModifiers = new StreamModifiers();
    private String connectionName;
    private StreamReader streamReader;
    private IAsyncHelper asyncHelper;
    private IOHandler ioHandler;
    private ILogger logger;
    private final EventRegister eventRegister;


    public BasicService(String connectionName, IAsyncHelper asyncHelper, IOHandler ioHandler, ILogger logger, IPacketDefinition packet) {
        this.connectionName = connectionName;
        this.asyncHelper = asyncHelper;
        this.ioHandler = ioHandler;
        this.streamReader = new StreamReader(packet);
        this.logger = logger.getLogger("connection\\"+connectionName);
        this.eventRegister = new EventRegister(logger);
    }


    public byte[] read(StreamChannel channel, byte[] bufferIn) {
        return read(channel, bufferIn, 0);
    }

    private final StreamChannel.DataProcessor dataProcessor = new StreamChannel.DataProcessor() {
        @Override
        public byte[] process(StreamChannel channel, byte[] in) {
            return streamReader.read(channel, in, BasicService.this, asyncHelper, "Stream Executor Thread for " + getConnectionName());
        }
    };

    public byte[] read(StreamChannel channel, byte[] in, int skip) {
        return channel.process(in, skip, dataProcessor);
    }


    public StreamModifiers getStreamModifiers() {
        return streamModifiers;
    }

    public String getConnectionName() {
        return connectionName;
    }

    public void connect() throws TimeoutException, InterruptedException, IOException {
        closed = false;
        openIOHandler();
    }

    public void disconnect(Throwable t) {
        disconnect(DisconnectReason.Error, t);
    }


    public void disconnect(DisconnectReason reason) {
        disconnect(reason, null);
    }

    private boolean closed = true;
    protected void disconnect(DisconnectReason reason, Throwable t) {
        if (!closed) {
            return;
        }
        closed = true;
        ioHandler.close();
        Validate.isTrue((t!=null)==(reason == DisconnectReason.Error));
        invokeEvent(new ConnectionClosedEvent<>(this, reason, t, this.getClass()));
    }

    /**Should be called server side to make the connection ready*/
    public void expectConnection() throws IOException {
        openIOHandler();
    }

    protected void openIOHandler() throws IOException {
        ioHandler.setConnection(this);
        ioHandler.open();
    }


    public boolean isConnected() {
        return ioHandler.isOpen();
    }

    protected void setConnectionName(String connectionName) {
        this.connectionName = connectionName;
        String newLoggerPrefix = "connection\\"+connectionName;
        ioHandler.updateConnectionName(connectionName);
        logger.changePrefix(newLoggerPrefix);
        eventRegister.setLoggerPrefix(newLoggerPrefix);
    }

    public IAsyncHelper getAsyncHelper() {
        return asyncHelper;
    }

    public IOHandler getIOHandler() {
        return ioHandler;
    }

    @Override
    public void send(ByteBuffer stream, boolean reliable) {
        stream = streamModifiers.apply(stream);
        BExStatic.insertInteger(stream.getLength(), stream);
        try {
            ioHandler.send(stream, reliable);
        } catch (IOException e) {
            onIOException(e);
        }
    }

    @Override
    public void send(ByteBuffer stream) {
        send(stream, true);
    }

    public ILogger getLogger() {
        return logger;
    }

    public void onIOException(IOException e) {
        if (isConnected()) {
            e.printStackTrace(System.out);
            throw new IllegalStateException("IOException occurred: "+e.toString(), e);
        }
    }

    @Override
    public boolean registerEventListener(Listener listener) {
        return eventRegister.registerEventListener(listener);
    }

    @Override
    public boolean unregisterEventListener(Listener listener) {
        return eventRegister.unregisterEventListener(listener);
    }

    @Override
    public void invokeEvent(Event event) {
        eventRegister.invokeEvent(event);
    }

    @Override
    public EventRegister getEventRegisterImplementation() {
        return eventRegister;
    }

    public EventRegister getEventRegister() {
        return eventRegister;
    }
}
