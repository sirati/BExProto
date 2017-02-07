package de.sirati97.bex_proto.threading;

/**
 * Created by sirati97 on 07.02.2017 for BexProto.
 */
public enum ShutdownBehavior {
    Daemon(false, true),
    /**
     * Might induce undefined behavior as threads will operate after shutdownHooks were invoked
     */
    JavaVMShutdownWait(true, true),
    JavaVMShutdown(true, true),
    JavaVMShutdownNow(true, true),
    ManualShutdown(false, false);

    private final boolean shutdownHook;
    private final boolean daemon;

    ShutdownBehavior(boolean shutdownHook, boolean daemon) {
        this.shutdownHook = shutdownHook;
        this.daemon = daemon;
    }

    public boolean isDaemon() {
        return daemon;
    }

    public boolean isShutdownHook() {
        return shutdownHook;
    }
}
