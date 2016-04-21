package de.sirati97.bex_proto.threading;

import java.util.concurrent.ExecutorService;

public interface AsyncHelper {
	AsyncTask runAsync(Runnable runnable, String name);
	ExecutorService createExecutorService(String name);
	
	public static interface AsyncTask {
		void stop();
		boolean isRunning();
		String getName();
		void setName(String name);
	}
}
