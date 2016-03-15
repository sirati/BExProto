package de.sirati97.bex_proto.v2.artifcon;

import de.sirati97.bex_proto.threading.AsyncHelper;
import de.sirati97.bex_proto.util.Sender;
import de.sirati97.bex_proto.v2.StreamReader;

import javax.crypto.Cipher;

/**
 * Created by sirati97 on 15.03.2016.
 */
public class ArtifConnection implements Sender{
    private Cipher sendCipher;
    private Cipher receiveCipher;
    private final String connectionName;
    private StreamReader streamReader;
    private AsyncHelper asyncHelper;

    public ArtifConnection(String connectionName, AsyncHelper asyncHelper) {
        this.connectionName = connectionName;
        this.asyncHelper = asyncHelper;
    }


    public byte[] read(byte[] overflow, byte[] in) {

        byte[] buffer = in;
        if (overflow != null) {
            byte[] buffer2 = new byte[overflow.length + buffer.length];
            System.arraycopy(overflow, 0, buffer2, 0, overflow.length);
            System.arraycopy(buffer, 0, buffer2, overflow.length, buffer.length);
            buffer = buffer2;
            overflow = null;
        }
        overflow = exercuteInput(buffer);
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
}
