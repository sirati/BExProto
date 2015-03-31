package de.sirati97.bex_proto.network;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


public class AdvThreadAsyncHelper implements AsyncHelper {
	private Set<Thread> activeThreads = new HashSet<>();
	
	
	@Override
	public AsyncTaskImpl runAsync(Runnable runnable, String name) {
		AdvRunnbale advRunnbale = new AdvRunnbale(runnable);
		Thread thread = new Thread(advRunnbale, name);
		advRunnbale.setThread(thread);
		thread.start();
		return new AsyncTaskImpl(thread);
	}
	
	public Set<Thread> getActiveThreads() {
		return Collections.unmodifiableSet(activeThreads);
	}
	
	class AdvRunnbale implements Runnable {
		private Runnable runnable;
		private Thread thread;
		
		
		public AdvRunnbale(Runnable runnable) {
			this.runnable = runnable;
		}
		
		@Override
		public void run() {
			synchronized (activeThreads) {
				activeThreads.add(thread);
			}
			try {
				runnable.run();
			} finally {
				synchronized (activeThreads) {
					activeThreads.remove(thread);
				}
			}
		}
		
		public void setThread(Thread thread) {
			this.thread = thread;
		}
	}
	
	static class AsyncTaskImpl implements AsyncTask {
		private Thread thread;

		public AsyncTaskImpl(Thread thread) {
			this.thread = thread;
		}
		
		@Override
		public void stop() {
			thread.interrupt();
			
		}
		
	}
}
