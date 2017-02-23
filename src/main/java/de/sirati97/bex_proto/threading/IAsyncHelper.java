package de.sirati97.bex_proto.threading;

import java.util.Set;
import java.util.concurrent.ExecutorService;

public interface IAsyncHelper {
	AsyncTask runAsync(Runnable runnable, String name);
	ExecutorService createExecutorService(String name);
	void stop();
	boolean isStopped();
	Set<? extends AsyncTask> getActiveTasks();

}
