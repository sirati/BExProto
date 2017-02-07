package de.sirati97.bex_proto.threading;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;


public class ThreadPoolAsyncHelper implements AsyncHelper {
    private static final AtomicLong unnamedCount = new AtomicLong(0);
    private final Set<AsyncTaskImpl> activeTasks = new HashSet<>();
	private final Set<AsyncTaskImpl> activeTasksRead = Collections.unmodifiableSet(activeTasks);
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

	public void stop() {
		executorService.shutdown();
	}

	@Override
	public boolean isStopped() {
		return executorService.isShutdown();
	}

	@Override
	public Set<? extends AsyncTask> getActiveTasks() {
		synchronized (activeTasks) {
			return activeTasksRead;
		}
	}

	@Override
	public AsyncTaskImpl runAsync(Runnable r, String name) {
		AsyncTaskImpl result = new AsyncTaskImpl(r, name);
		executorService.execute(result.getTask());
		return result;
	}

	@Override
	public AsyncHelperExecutorService createExecutorService(String name) {
		return new AsyncHelperExecutorService(this, name);
	}

	protected void onUncaughtException(Throwable t) {
		t.printStackTrace();
	}

	class AsyncTaskImpl extends AsyncTaskBase{
		public AsyncTaskImpl(Runnable runnable, String name) {
			super(runnable, name);
		}

		@Override
		protected void beforeExecution() {
			synchronized (activeTasks) {
				activeTasks.add(this);
			}
		}

		@Override
		protected void afterExecution() {
			synchronized (activeTasks) {
				activeTasks.remove(this);
			}
		}

		@Override
		protected void onUncaughtException(Throwable t) {
			ThreadPoolAsyncHelper.this.onUncaughtException(t);
		}

        @Override
        protected void resetThreadName(Thread t) {
            BExThreadFactory.BExThread thread = (BExThreadFactory.BExThread)t;
            thread.resetBExName();
        }

        @Override
        protected Task createTask(Runnable runnable, String name) {
            return new Task(runnable, name) {
                public void setName(String name) {
                    if (getThread()!=null)((BExThreadFactory.BExThread)getThread()).setBExName(name);
                    super.setName(name, false);
                }
            };
        }
    }
}
