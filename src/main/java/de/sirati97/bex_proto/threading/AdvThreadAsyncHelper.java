package de.sirati97.bex_proto.threading;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class AdvThreadAsyncHelper implements AsyncHelper {
	private final Set<Thread> activeThreads = new HashSet<>();
	private final Set<Thread> activeThreadsRead = Collections.unmodifiableSet(activeThreads);
	private ExecutorService executorService;
	
	public AdvThreadAsyncHelper() {
        executorService = Executors.newCachedThreadPool();

		
	}

	public void stop() {
		executorService.shutdown();
	}
	
	@Override
	public AsyncTaskImpl runAsync(Runnable r, String name) {
		AdvRunnable advRunnable = new AdvRunnable(r, name);
		executorService.execute(advRunnable);
		return new AsyncTaskImpl(advRunnable);
	}
	
	public Set<Thread> getActiveThreads() {
		synchronized (activeThreads) {
			return activeThreadsRead;
		}
	}

	@Override
	public AsyncHelperExecutorService createExecutorService(String name) {
		return new AsyncHelperExecutorService(this, name);
	}
	

	
	class AdvRunnable implements Runnable {
		private Runnable runnable;
		boolean running = false;
		private String name;
		private Thread thread = null;
		
		public AdvRunnable(Runnable runnable, String name) {
			this.runnable = runnable;
			this.name = name;
		}
		
		@Override
		public void run() {
			thread = Thread.currentThread();
			thread.setName(name);
			synchronized (activeThreads) {
				activeThreads.add(thread);
			}
			running = true;
			try {
				runnable.run();
			} catch(Throwable t) {
				t.printStackTrace();
			} finally {
				synchronized (activeThreads) {
					activeThreads.remove(thread);
				}
				running = false;
				thread.setName("Thread finished execution. Waiting on next Task.");
			}
		}
		
		public boolean isRunning() {
			return running;
		}
		
		public String getName() {
			return name;
		}
		
		public void setName(String name) {
			if (thread!=null)thread.setName(name);
			this.name = name;
		}
		
		public void stop() {
			if (thread!=null)thread.interrupt();
		}
		
	}
	
	static class AsyncTaskImpl implements AsyncTask {
		private AdvRunnable advRunnable;

		public AsyncTaskImpl(AdvRunnable advRunnable) {
			this.advRunnable = advRunnable;
		}
		
		@Override
		public void stop() {
			advRunnable.stop();
		}


		@Override
		public boolean isRunning() {
			return advRunnable.isRunning();
		}

		@Override
		public String getName() {
			return advRunnable.getName();
		}

		@Override
		public void setName(String name) {
			advRunnable.setName(name);
		}
		
	}
}
