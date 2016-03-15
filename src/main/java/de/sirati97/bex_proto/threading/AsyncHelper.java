package de.sirati97.bex_proto.threading;

public interface AsyncHelper {
	AsyncTask runAsync(Runnable runnable, String name);
	
	public static interface AsyncTask {
		void stop();
		boolean isRunning();
		String getName();
		void setName(String name);
	}
}
