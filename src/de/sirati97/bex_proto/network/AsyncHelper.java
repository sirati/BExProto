package de.sirati97.bex_proto.network;

public interface AsyncHelper {
	AsyncTask runAsync(Runnable runnable);
	
	public static interface AsyncTask {
		void stop();
	}
}
