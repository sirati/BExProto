package de.sirati97.bex_proto.v2.artifcon;

import de.sirati97.bex_proto.datahandler.CryptoStream;
import de.sirati97.bex_proto.datahandler.SendStream;
import de.sirati97.bex_proto.threading.AsyncHelper;
import de.sirati97.bex_proto.util.IConnection;
import de.sirati97.bex_proto.util.logging.ILogger;
import de.sirati97.bex_proto.v2.IPacket;
import de.sirati97.bex_proto.v2.StreamReader;

import javax.crypto.Cipher;

/**
 * Created by sirati97 on 15.03.2016.
 */
public class ArtifConnection implements IConnection {
    private Cipher sendCipher;
    private Cipher receiveCipher;
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

    public String getConnectionName() {
        return connectionName;
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
    public void send(SendStream stream) {
        ioHandler.send((getSendCipher()==null?stream:new SendStream(new CryptoStream(stream.getHeadlessStream(), getSendCipher()))).getBytes());
    }

    public ILogger getLogger() {
        return logger;
    }
}
