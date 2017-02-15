package de.sirati97.bex_proto.v2.io;

import de.sirati97.bex_proto.util.bytebuffer.ByteBuffer;
import de.sirati97.bex_proto.v2.StreamChannel;
import de.sirati97.bex_proto.v2.service.basic.BasicService;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

/**
 * Created by sirati97 on 13.04.2016.
 */
public class TestIOHandler extends StreamChannel implements IOHandler {
    private BasicService connection;
    public TestIOHandler receiver;
    private boolean open = false;
    private boolean closed = false;
    private final Object lock = new Object();

    @Override
    public synchronized void send(ByteBuffer stream, boolean reliable) throws IOException {
        if (!open) {
            throw new IOException("Pipe is not open");
        }
        System.out.println(bytesToString(stream.getBytes()));
        System.out.println(bytesToString2(stream.getBytes()));
//        System.out.println(new String(stream.getBytes()).replace('\n',' ').replace('\r',' '));
        receiver.read(stream.getBytes());
    }

    public void read(byte[] stream) throws IOException {
        synchronized (lock) {
            if (!open && !closed) {
                try {
                    lock.wait(); //will wait until it is opened
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        if (closed) {
            throw new IOException("Pipe is not open");
        }
        connection.read(this, stream);
    }

    @Override
    public boolean isOpen() {
        return open;
    }

    @Override
    public synchronized void close() {
        open = false;
        closed = true;
    }

    @Override
    public synchronized void open() throws IOException {
        if (receiver == null) {
            throw new IOException("Receiver not set");
        }
        synchronized (lock) {
            lock.notifyAll();
        }
        open = true;
    }

    @Override
    public synchronized void setConnection(BasicService connection) {
        this.connection = connection;
    }

    @Override
    public void updateConnectionName(String newName) {

    }


    public static String bytesToString(byte[] stream) {
        StringBuilder sb = new StringBuilder();
        for (byte b:stream) {
            int i = (int)b & 0x000000FF;
            char c = (char)i;
            String num = String.valueOf(i);
            sb.append(StringUtils.leftPad(num, 4 , ' '));
//            sb.append('\'');
//            if (i <= 32) {
//                sb.append((char)(i+256));
//            } else if (i==95) {
//                sb.append((char)(289));
//            } else if (i>=127 &&i<=170) {
//                sb.append((char)(i+163));
//            } else {
//                sb.append(c);
//            }
//            sb.append('\'');
        }
        return sb.toString();
    }
    public static String bytesToString2(byte[] stream) {
        StringBuilder sb = new StringBuilder();
        for (byte b:stream) {
            int i = (int)b & 0x000000FF;
            char c = (char)i;
            sb.append("   ");
            if (i <= 32) {
                sb.append((char)(i+256));
            } else if (i==95) {
                sb.append((char)(289));
            } else if (i>=127 &&i<=170) {
                sb.append((char)(i+163));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
//
//    public static void main(String... args) {
//        byte[] b = new byte[256];
//        for (int i = 0; i < b.length; i++) {
//            b[i] = (byte) i;
//        }
//        System.out.println(bytesToString(b));
//        System.out.println(bytesToString2(b));
//
//    }


}
