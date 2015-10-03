package de.sirati97.bex_proto.network;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.TimeUnit;

public class AsyncHelperExecutorService extends AbstractExecutorService{
	private AsyncHelper asyncHelper;
	private String name;
	private int id=0;
	
	public AsyncHelperExecutorService(AsyncHelper asyncHelper, String name) {
		this.asyncHelper = asyncHelper;
		this.name = name;
	}
	
	@Override
	public boolean awaitTermination(long arg0, TimeUnit arg1) throws InterruptedException {
		return false;
	}

	@Override
	public boolean isShutdown() {
		return false;
	}

	@Override
	public boolean isTerminated() {
		return false;
	}

	@Override
	public void shutdown() {
	}

	@Override
	public List<Runnable> shutdownNow() {
		return Collections.emptyList();
	}

	@Override
	public void execute(Runnable runnable) {
		asyncHelper.runAsync(runnable, name + "-" + id);
	}


}
