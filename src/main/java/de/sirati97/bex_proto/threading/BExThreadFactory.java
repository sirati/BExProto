package de.sirati97.bex_proto.threading;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by sirati97 on 07.02.2017 for BexProto.
 */
public class BExThreadFactory implements ThreadFactory {
    private final String prefix;
    private final String standardName;
    private final boolean daemon;
    private final AtomicLong count = new AtomicLong(0);


    public BExThreadFactory(String prefix, boolean daemon) {
        this(prefix, "Unnamed", daemon);
    }

    public BExThreadFactory(String prefix, String standardName, boolean daemon) {
        this.prefix = prefix;
        this.standardName = standardName;
        this.daemon = daemon;
    }

    @Override
    public Thread newThread(Runnable r) {
        return new BExThread(r, prefix, count.getAndIncrement(), daemon);
    }

    public class BExThread extends Thread{
        private String prefix;
        private final long factoryId;

        public BExThread(Runnable r, String prefix, long factoryId, boolean daemon) {
            super(r);
            this.prefix = prefix;
            this.factoryId = factoryId;
            setDaemon(daemon);
            resetBExName();
        }

        public String getPrefix() {
            return prefix;
        }

        public long getFactoryId() {
            return factoryId;
        }

        public void setBExName(String name) {
            StringBuilder sb = new StringBuilder(prefix);
            sb.append('-');
            sb.append(factoryId);
            sb.append(' ');
            sb.append(name);
            setName(sb.toString());
        }

        public void resetBExName() {
            setBExName(standardName);
        }
    }
}
