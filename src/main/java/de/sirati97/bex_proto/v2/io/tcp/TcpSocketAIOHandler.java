package de.sirati97.bex_proto.v2.io.tcp;

import de.sirati97.bex_proto.util.bytebuffer.ByteBufferSegment;
import de.sirati97.bex_proto.v2.io.IOHandlerBase;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
    protected void sendInternal(de.sirati97.bex_proto.util.bytebuffer.ByteBuffer stream, boolean reliable) throws IOException {
        ByteBuffer nioByteBuffer = ByteBuffer.allocateDirect(stream.getLength());
        for (ByteBufferSegment segment:stream) {
            nioByteBuffer.put(segment.getBytes(), segment.getOffset(), segment.getLength());
        }
        nioByteBuffer.flip();
        Future f = socket.write(nioByteBuffer);
        try {
            f.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new IOException(e);
        }
//        finally {
//            try {
//                unallocateDirectByteBuffer(nioByteBuffer);
//            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
//                throw new IOException(e);
//            }
//        }
    }

    private static void unallocateDirectByteBuffer(ByteBuffer buffer)
            throws IllegalArgumentException, IllegalAccessException,
            InvocationTargetException, SecurityException, NoSuchMethodException {

        if (!buffer.isDirect()) {
            throw new IllegalStateException("ByteBuffer is not direct");
        }

        Method cleanerMethod = buffer.getClass().getMethod("cleaner");
        cleanerMethod.setAccessible(true);
        Object cleaner = cleanerMethod.invoke(buffer);
        Method cleanMethod = cleaner.getClass().getMethod("clean");
        cleanMethod.setAccessible(true);
        cleanMethod.invoke(cleaner);
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
                if (result < 0){ //remote peer has closed channel
                    getConnection().disconnect();
                    return;
                }
                buffer.flip();
                byte[] read = new byte[result];
                buffer.get(read);
//                System.out.println("read bytes: " + TestIOHandler.bytesToString(read));
                read();
                getConnection().read(read);
            }

            @Override
            public void failed(Throwable exc, ByteBuffer attachment) {
                if (!isOpen() || exc instanceof InterruptedException || exc instanceof ClosedChannelException || exc instanceof IOException) {
                    getConnection().disconnect();
                    return;
                }
                getConnection().onIOException(new IOException(exc));
                read();
            }
        });

    }
}
