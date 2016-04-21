package de.sirati97.bex_proto.v2.artifcon;

import de.sirati97.bex_proto.datahandler.EncryptionStream;
import de.sirati97.bex_proto.datahandler.HashStream;
import de.sirati97.bex_proto.datahandler.SendStream;
import de.sirati97.bex_proto.threading.AsyncHelper;
import de.sirati97.bex_proto.util.IConnection;
import de.sirati97.bex_proto.util.logging.ILogger;
import de.sirati97.bex_proto.v2.IPacket;
import de.sirati97.bex_proto.v2.StreamReader;
import de.sirati97.bex_proto.v2.io.IOHandler;

import javax.crypto.Cipher;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.concurrent.TimeoutException;

/**
 * Created by sirati97 on 15.03.2016.
 */
public class ArtifConnection implements IConnection {
    private Cipher sendCipher;
    private Cipher receiveCipher;
    private MessageDigest hashAlgorithm;
    private String connectionName;
    private StreamReader streamReader;
    private AsyncHelper asyncHelper;
    private byte[] overflow;
    private IOHandler ioHandler;
    private ILogger logger;

    public ArtifConnection(String connectionName, AsyncHelper asyncHelper, IOHandler ioHandler, ILogger logger, IPacket packet) {
        this.connectionName = connectionName;
        this.asyncHelper = asyncHelper;
        this.ioHandler = ioHandler;
        this.streamReader = new StreamReader(packet);
        this.logger = logger.getLogger("connection\\"+connectionName);
    }


    public byte[] read(byte[] bufferIn) {
        return read(bufferIn, 0);
    }

    public byte[] read(byte[] bufferIn, int skip) {
        if (overflow != null) {
            byte[] buffer2 = new byte[overflow.length + bufferIn.length-skip];
            System.arraycopy(overflow, 0, buffer2, 0, overflow.length);
            System.arraycopy(bufferIn, skip, buffer2, overflow.length, bufferIn.length-skip);
            bufferIn = buffer2;
            System.out.println(bufferIn.length + new String(bufferIn));
        } else if (skip > 0) {
            byte[] buffer2 = new byte[bufferIn.length-skip];
            System.arraycopy(bufferIn, skip, buffer2, 0, bufferIn.length-skip);
            bufferIn = buffer2;
        }
        overflow = executeInput(bufferIn);
        return overflow;
    }

    private Object executeInputMutex = new Object();
    protected synchronized byte[] executeInput(byte[] received) {
        synchronized (executeInputMutex) {
            return streamReader.read(received, this, asyncHelper, "Stream Executor Thread for " + getConnectionName());
        }
    }


    public Cipher getSendCipher() {
        return sendCipher;
    }

    public void setSendCipher(Cipher sendCipher) {
        this.sendCipher = sendCipher;
    }

    public Cipher getReceiveCipher() {
        return receiveCipher;
    }

    public void setReceiveCipher(Cipher receiveCipher) {
        this.receiveCipher = receiveCipher;
    }

    public MessageDigest getHashAlgorithm() {
        return hashAlgorithm;
    }

    public void setHashAlgorithm(MessageDigest hashAlgorithm) {
        this.hashAlgorithm = hashAlgorithm;
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
        logger.changePrefix("connection\\"+connectionName);
    }

    public AsyncHelper getAsyncHelper() {
        return asyncHelper;
    }

    public IOHandler getIoHandler() {
        return ioHandler;
    }

    @Override
    public void send(SendStream stream, boolean reliable) {
        if (getHashAlgorithm()!= null) {
            stream = new SendStream(new HashStream(stream.getHeadlessStream(), getHashAlgorithm()));
        }
        if (getSendCipher()!= null) {
            stream = new SendStream(new EncryptionStream(stream.getHeadlessStream(), getSendCipher()));
        }
        try {
            ioHandler.send(stream.getByteBuffer().getBytes(), reliable);
        } catch (IOException e) {
            onIOException(e);
        }
    }

    @Override
    public void send(SendStream stream) {
        send(stream, true);
    }

    public ILogger getLogger() {
        return logger;
    }

    public void onIOException(IOException e) {
        throw new IllegalStateException("IOException occurred: "+e.toString(), e);
    }
}
