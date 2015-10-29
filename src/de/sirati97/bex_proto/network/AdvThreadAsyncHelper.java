package de.sirati97.bex_proto.network;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class AdvThreadAsyncHelper implements AsyncHelper {
	private Set<Thread> activeThreads = new HashSet<>();
//	private ThreadFactory threadFactory;
	private ExecutorService executorService;
	
	public AdvThreadAsyncHelper() {
//		threadFactory = (new ThreadFactoryBuilder()).build();
        executorService = Executors.newFixedThreadPool(1000);

		
	}
	
	@Override
	public AsyncTaskImpl runAsync(Runnable r, String name) {
		AdvRunnbale advRunnbale = new AdvRunnbale(r, name);
		//Thread thread = threadFactory.newThread(advRunnbale);//new Thread(advRunnbale, name);
		executorService.execute(advRunnbale);
//		thread.setName(name);
//		advRunnbale.setThread(thread);
//		thread.start();
		return new AsyncTaskImpl(advRunnbale);
	}
	
	public Set<Thread> getActiveThreads() {
		synchronized (activeThreads) {
			return Collections.unmodifiableSet(activeThreads);
		}
	}
	

	
	class AdvRunnbale implements Runnable {
		private Runnable runnable;
		boolean running = false;
		private String name;
		private Thread thread = null;
		
		public AdvRunnbale(Runnable runnable, String name) {
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
				thread.setName("Thread finished exercution. Waiting on next Task.");
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
		private AdvRunnbale advRunnbale;

		public AsyncTaskImpl(AdvRunnbale advRunnbale) {
			this.advRunnbale = advRunnbale;
		}
		
		@Override
		public void stop() {
			advRunnbale.stop();
		}


		@Override
		public boolean isRunning() {
			return advRunnbale.isRunning();
		}

		@Override
		public String getName() {
			return advRunnbale.getName();
		}

		@Override
		public void setName(String name) {
			advRunnbale.setName(name);
		}
		
	}
}
