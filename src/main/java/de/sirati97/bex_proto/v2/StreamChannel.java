package de.sirati97.bex_proto.v2;

/**
 * Created by sirati97 on 11.02.2017 for BexProto.
 */
public class StreamChannel {
    private volatile byte[] unprocessed;

    public final synchronized byte[] process(byte[] in, int skip, DataProcessor processor) {
        unprocessed = processor.process(this, combine(in, skip));
        return unprocessed;
    }

    private synchronized byte[] combine(byte[] in, int skip) {
        if (unprocessed != null) {
            byte[] buffer2 = new byte[unprocessed.length + in.length-skip];
            System.arraycopy(unprocessed, 0, buffer2, 0, unprocessed.length);
            System.arraycopy(in, skip, buffer2, unprocessed.length, in.length-skip);
            in = buffer2;
        } else if (skip > 0) {
            byte[] buffer2 = new byte[in.length-skip];
            System.arraycopy(in, skip, buffer2, 0, in.length-skip);
            in = buffer2;
        }
        unprocessed = null;
        return in;
    }

    public interface DataProcessor {
        byte[] process(StreamChannel channel, byte[] in);
    }
}
