package de.sirati97.bex_proto.network;

import de.sirati97.bex_proto.util.exception.NotImplementedException;

import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class ScheduledAsyncHelperExecutorService
		extends AsyncHelperExecutorService
		implements ScheduledExecutorService {
	@SuppressWarnings("unused")
	private boolean alive = true;
	
	public ScheduledAsyncHelperExecutorService(AsyncHelper asyncHelper, String name) {
		super(asyncHelper, name);
		throw new NotImplementedException("");
	}

	@Override
	public ScheduledFuture<?> schedule(Runnable arg0, long arg1, TimeUnit arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <V> ScheduledFuture<V> schedule(Callable<V> arg0, long arg1, TimeUnit arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ScheduledFuture<?> scheduleAtFixedRate(Runnable arg0, long arg1, long arg2, TimeUnit arg3) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ScheduledFuture<?> scheduleWithFixedDelay(Runnable arg0, long arg1, long arg2, TimeUnit arg3) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void shutdown() {
		super.shutdown();
		alive = false;
	}

}