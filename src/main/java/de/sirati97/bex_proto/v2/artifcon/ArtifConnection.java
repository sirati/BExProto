package de.sirati97.bex_proto.v2.artifcon;

import de.sirati97.bex_proto.datahandler.CryptoStream;
import de.sirati97.bex_proto.datahandler.SendStream;
import de.sirati97.bex_proto.threading.AsyncHelper;
import de.sirati97.bex_proto.util.IConnection;
import de.sirati97.bex_proto.v2.StreamReader;

import javax.crypto.Cipher;

/**
 * Created by sirati97 on 15.03.2016.
 */
public class ArtifConnection implements IConnection {
    private Cipher sendCipher;
    private Cipher receiveCipher;
    private final String connectionName;
    private StreamReader streamReader;
    private AsyncHelper asyncHelper;
    private byte[] overflow;
    private IOHandler ioHandler;

    public ArtifConnection(String connectionName, AsyncHelper asyncHelper, IOHandler ioHandler) {
        this.connectionName = connectionName;
        this.asyncHelper = asyncHelper;
        this.ioHandler = ioHandler;
    }


    public byte[] read(byte[] bufferIn, int skip) {
        if (overflow != null) {
            byte[] buffer2 = new byte[overflow.length + bufferIn.length-skip];
            System.arraycopy(overflow, 0, buffer2, 0, overflow.length);
            System.arraycopy(bufferIn, skip, buffer2, overflow.length, bufferIn.length-skip);
            bufferIn = buffer2;
        }
        overflow = exercuteInput(bufferIn);
        return overflow;
    }

    private Object exercuteInputMetux = new Object();
    protected synchronized byte[] exercuteInput(byte[] received) {
        synchronized (exercuteInputMetux) {
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

    @Override
    public void send(SendStream stream) {
        ioHandler.send((getSendCipher()==null?stream:new SendStream(new CryptoStream(stream.getHeadlessStream(), getSendCipher()))).getBytes());
    }


}
