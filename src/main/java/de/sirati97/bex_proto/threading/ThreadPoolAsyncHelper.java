package de.sirati97.bex_proto.threading;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;


public class ThreadPoolAsyncHelper extends AsyncHelperBase {
    private static final AtomicLong unnamedCount = new AtomicLong(0);
	private ExecutorService executorService;
	
	public ThreadPoolAsyncHelper(ShutdownBehavior shutdownBehavior) {
	    this(shutdownBehavior, "Pool" + unnamedCount.getAndIncrement());
    }

	public ThreadPoolAsyncHelper(final ShutdownBehavior shutdownBehavior, String name) {
        this.executorService = Executors.newCachedThreadPool(new BExThreadFactory(name, "Unused", shutdownBehavior.isDaemon()));
        if (shutdownBehavior.isShutdownHook()) {
            Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (ShutdownBehavior.JavaVMShutdownWait.equals(shutdownBehavior)) {
                            while (activeTasks.size()>0) {
                                Thread.sleep(50);
                            }
                            executorService.shutdownNow();
                        } else {
                            if (ShutdownBehavior.JavaVMShutdown.equals(shutdownBehavior)) {
                                executorService.shutdown();
                            } else if (ShutdownBehavior.JavaVMShutdownNow.equals(shutdownBehavior)) {
                                executorService.shutdownNow();
                            }
                            executorService.awaitTermination(30, TimeUnit.SECONDS);
                        }


                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }, "Shotdown hook for " + name));
        }
	}

    @Override
	public void stop() {
		executorService.shutdown();
	}

	@Override
	public boolean isStopped() {
		return executorService.isShutdown();
	}


    @Override
    protected void invokeTask(AsyncTaskImpl.Task task) {
        executorService.execute(task);
    }

    @Override
    protected void onUncaughtException(Throwable t) {
		t.printStackTrace();
	}

    @Override
    protected void resetThreadName(Thread t) {
        ((BExThreadFactory.BExThread)t).resetBExName();
    }

    @Override
    protected void setThreadName(Thread t, String name) {
        ((BExThreadFactory.BExThread)t).setBExName(name);
    }
}
