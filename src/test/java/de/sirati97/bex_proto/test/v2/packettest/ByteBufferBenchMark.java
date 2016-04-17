package de.sirati97.bex_proto.test.v2.packettest;

import de.sirati97.bex_proto.util.bytebuffer.ByteBuffer;
import org.junit.Test;

/**
 * Created by sirati97 on 17.04.2016.
 */
public class ByteBufferBenchMark {
    private final byte[] bytes = new byte[] {1,7,34,7,3,1,7,0,7,-5,1,7,34,7,3,1,7,0,7,-5,1,7,34,7,3,1,7,0,7,-5,1,7,34,7,3,1,7,0,7,-5,1,7,34,7,3,1,7,0,7,-5,1,7,34,7,3,1,7,0,7,-5,1,7,34,7,3,1,7,0,7,-5,1,7,34,7,3,1,7,0,7,-5,1,7,34,7,3,1,7,0,7,-5,1,7,34,7,3,1,7,0,7,-5,1,7,34,7,3,1,7,0,7,-5,1,7,34,7,3,1,7,0,7,-5,1,7,34,7,3,1,7,0,7,-5,1,7,34,7,3,1,7,0,7,-5,1,7,34,7,3,1,7,0,7,-5,1,7,34,7,3,1,7,0,7,-5,1,7,34,7,3,1,7,0,7,-5,1,7,34,7,3,1,7,0,7,-5,1,7,34,7,3,1,7,0,7,-5,1,7,34,7,3,1,7,0,7,-5};
    private final static int runs=100000000;
    private final static int arrays_root= test(2);
    private final static int arrays=arrays_root*arrays_root;

    public static int test(int arrays_root) {
        System.out.println("MUHUHUHU");
        return (int) Math.sqrt(arrays_root*arrays_root);
    }

    @Test
    public void start() throws Throwable {
        try {
            System.out.println("Preparing Benchmark");
            System.out.println("JIT ArrayCopy");
            int length1=benchArray();
            System.out.println("JIT ByteBuffer");
            int length2=benchBuffer();
            System.out.println(length1+":"+length2);

            System.out.println("Start ArrayCopy Benchmark");
            System.gc();
            long timestamp = System.nanoTime();
            benchArray();
            System.out.println("ArrayCopy benchmark took " +((System.nanoTime()-timestamp)/1000000) + "ms");
            System.out.println("Start ByteBuffer Benchmark");
            System.gc();
            timestamp = System.nanoTime();
            benchBuffer();
            System.out.println("ByteBuffer benchmark took " +((System.nanoTime()-timestamp)/1000000) + "ms");
        } catch (Throwable e) {
            e.printStackTrace();
            throw e;
        }

    }

    int benchArray() {
        int result=0;
        for (int i = 0;i<runs;i++) {
            result+=_benchArray();
        }
        return result;
    }

    int _benchArray() {
        byte[] last = bytes;
        for (int i = 0;i<arrays-1;i++) {
            byte[] news = new byte[(i+2)*bytes.length];
            System.arraycopy(last,0,news,0,last.length);
            System.arraycopy(bytes,0,news,last.length,bytes.length);
            last = news;
        }
        return last.length;
    }

    int benchBuffer() {
        int result=0;
        for (int i = 0;i<runs;i++) {
            result+=_benchBuffer();
        }
        return result;
    }
    int _benchBuffer() {
        ByteBuffer buffer = new ByteBuffer();
        for (int i = 0;i<arrays_root;i++) {
            ByteBuffer buffer2 = new ByteBuffer(bytes);
            for (int j = 1;j<arrays_root;j++) {
                buffer2.append(bytes);
            }
            buffer.append(buffer2);
        }
        buffer.seal();
        return buffer.getBytes().length;
    }
}
