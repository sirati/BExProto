package de.sirati97.bex_proto.network;


public class ThreadAsyncHelper implements AsyncHelper {

	@Override
	public AsyncTaskImpl runAsync(Runnable runnable, String name) {
		Thread thread = new Thread(runnable, name);
		thread.start();
		return new AsyncTaskImpl(thread);
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
