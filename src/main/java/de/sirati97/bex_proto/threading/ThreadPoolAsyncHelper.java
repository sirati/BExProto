package de.sirati97.bex_proto.threading;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ThreadPoolAsyncHelper implements AsyncHelper {
	private final Set<AsyncTaskImpl> activeTasks = new HashSet<>();
	private final Set<AsyncTaskImpl> activeTasksRead = Collections.unmodifiableSet(activeTasks);
	private ExecutorService executorService;
	
	public ThreadPoolAsyncHelper() {
        executorService = Executors.newCachedThreadPool();

		
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
		protected Task getTask() {
			return super.getTask();
		}
	}
}
