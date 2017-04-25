package de.sirati97.bex_proto.v2.io.tcp;

import de.sirati97.bex_proto.threading.AsyncTask;
import de.sirati97.bex_proto.util.bytebuffer.ByteBuffer;
import de.sirati97.bex_proto.util.bytebuffer.IByteBufferSegment;
import de.sirati97.bex_proto.v2.io.IOHandlerBase;
import de.sirati97.bex_proto.v2.service.basic.DisconnectReason;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

/**
 * Created by sirati97 on 17.04.2016.
 */
public class TcpSocketBIOHandler extends IOHandlerBase {
    private final Socket socket;
    private AsyncTask listenerTask;

    public TcpSocketBIOHandler(Socket socket) {
        this.socket = socket;
    }


    @Override
    protected void sendInternal(ByteBuffer stream, boolean reliable) throws IOException {
        for (IByteBufferSegment segment:stream) {
            socket.getOutputStream().write(segment.getBytes(), segment.getOffset(), segment.getLength());
        }
        socket.getOutputStream().flush();
    }

    @Override
    protected boolean isIOOpen() {
        return socket.isConnected();
    }

    @Override
    protected void closeIO() throws IOException {
        if (!socket.isClosed()) {
            socket.getOutputStream().flush();
            socket.close();
        }
    }

    @Override
    protected void onClose() {
        if (listenerTask != null && listenerTask.isRunning()) {
            listenerTask.stop();
        }
    }
    @Override
    protected void startReading() throws IOException{
        listenerTask = getConnection().getAsyncHelper().runAsync(new Runnable() {
            @Override
            public void run() {
                try {
                    InputStream in = socket.getInputStream();
                    while (getOpenFlag() && !Thread.interrupted()) {
                        try {
                            int available;
                            if ((available=in.available()) > 0) {
                                byte[] read = new byte[available];
                                if(in.read(read)==-1) {
                                    socket.shutdownOutput();
                                    getConnection().disconnect(DisconnectReason.RemoteDisconnected);
                                    return;
                                }
                                getConnection().read(TcpSocketBIOHandler.this, read);
                            } else {
                                Thread.sleep(0, 1);
                            }
                        } catch (InterruptedException e) {
                            getConnection().disconnect(e);
                            break;
                        }
                    }
                } catch (IOException e) {
                    getConnection().disconnect(e);
                } finally {
                    if (getOpenFlag()) {
                        getConnection().disconnect(DisconnectReason.Abnormal);
                    }
                }
            }
        },getConnection().getConnectionName() + " Listener Thread");
    }


    @Override
    public void updateConnectionName(String newName) {
        if (listenerTask!=null) {
            listenerTask.setName(newName + " Listener Thread");
        }
    }
}
