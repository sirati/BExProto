package de.sirati97.bex_proto.v2.service.basic;

import de.sirati97.bex_proto.datahandler.BExStatic;
import de.sirati97.bex_proto.events.Event;
import de.sirati97.bex_proto.events.EventRegister;
import de.sirati97.bex_proto.events.IEventRegister;
import de.sirati97.bex_proto.events.Listener;
import de.sirati97.bex_proto.threading.AsyncHelper;
import de.sirati97.bex_proto.util.IConnection;
import de.sirati97.bex_proto.util.bytebuffer.ByteBuffer;
import de.sirati97.bex_proto.util.logging.ILogger;
import de.sirati97.bex_proto.v2.IPacketDefinition;
import de.sirati97.bex_proto.v2.StreamReader;
import de.sirati97.bex_proto.v2.io.IOHandler;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by sirati97 on 15.03.2016.
 */
public class BasicService implements IConnection, IEventRegister {
    private final StreamModifiers streamModifiers = new StreamModifiers();
    private String connectionName;
    private StreamReader streamReader;
    private AsyncHelper asyncHelper;
    private byte[] overflow;
    private IOHandler ioHandler;
    private ILogger logger;
    private final EventRegister eventRegister;


    public BasicService(String connectionName, AsyncHelper asyncHelper, IOHandler ioHandler, ILogger logger, IPacketDefinition packet) {
        this.connectionName = connectionName;
        this.asyncHelper = asyncHelper;
        this.ioHandler = ioHandler;
        this.streamReader = new StreamReader(packet);
        this.logger = logger.getLogger("connection\\"+connectionName);
        this.eventRegister = new EventRegister(logger);
    }


    public byte[] read(byte[] bufferIn) {
        return read(bufferIn, 0);
    }


    private Object readMutex = new Object();
    public byte[] read(byte[] bufferIn, int skip) {
        synchronized (readMutex) {
            if (overflow != null) {
                byte[] buffer2 = new byte[overflow.length + bufferIn.length-skip];
                System.arraycopy(overflow, 0, buffer2, 0, overflow.length);
                System.arraycopy(bufferIn, skip, buffer2, overflow.length, bufferIn.length-skip);
                bufferIn = buffer2;
                //System.out.println(bufferIn.length + new String(bufferIn));
            } else if (skip > 0) {
                byte[] buffer2 = new byte[bufferIn.length-skip];
                System.arraycopy(bufferIn, skip, buffer2, 0, bufferIn.length-skip);
                bufferIn = buffer2;
            }
            overflow = executeInput(bufferIn);
            return overflow;
        }
    }

    protected byte[] executeInput(byte[] received) {
            return streamReader.read(received, this, asyncHelper, "Stream Executor Thread for " + getConnectionName());
    }

    public StreamModifiers getStreamModifiers() {
        return streamModifiers;
    }

    public String getConnectionName() {
        return connectionName;
    }

    public void connect() throws TimeoutException, InterruptedException, IOException {
        openIOHandler();
    }

    public void disconnect() {
        ioHandler.close();
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

    public AsyncHelper getAsyncHelper() {
        return asyncHelper;
    }

    public IOHandler getIoHandler() {
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
