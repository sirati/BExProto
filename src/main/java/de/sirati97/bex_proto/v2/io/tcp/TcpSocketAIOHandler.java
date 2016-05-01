package de.sirati97.bex_proto.v2.io.tcp;

import de.sirati97.bex_proto.v2.io.IOHandlerBase;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created by sirati97 on 17.04.2016.
 */
public class TcpSocketAIOHandler extends IOHandlerBase {
    private final AsynchronousSocketChannel socket;
    private ByteBuffer nioByteBuffer = ByteBuffer.allocateDirect(4096);

    public TcpSocketAIOHandler(AsynchronousSocketChannel socket) {
        this.socket = socket;
    }


    @Override
    protected void sendInternal(byte[] stream, boolean reliable) throws IOException {
        Future f = socket.write(ByteBuffer.wrap(stream));
        try {
            f.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new IOException(e);
        }
    }

    @Override
    protected boolean isIOOpen() {
        return socket.isOpen();
    }

    @Override
    protected void closeIO() throws IOException {
        socket.close();
    }

    @Override
    protected void onClose() {}

    @Override
    protected void startReading() throws IOException{
        read();
    }

    private void read() {
        if (!isOpen()){
            return;
        }
        nioByteBuffer.clear();
        socket.read(nioByteBuffer, nioByteBuffer, new CompletionHandler<Integer, ByteBuffer>() {
            @Override
            public void completed(Integer result, ByteBuffer buffer) {
                if (result < 0){ //remote peer has closen channel
                    getConnection().disconnect();
                    return;
                }
                buffer.flip();
                byte[] read = new byte[result];
                buffer.get(read);
                read();
                getConnection().read(read);
            }

            @Override
            public void failed(Throwable exc, ByteBuffer attachment) {
                if (!isOpen() || exc instanceof InterruptedException || exc instanceof ClosedChannelException || exc instanceof IOException) {
                    return;
                }
                getConnection().onIOException(new IOException(exc));
                read();
            }
        });

    }
}
