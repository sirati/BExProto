package de.sirati97.bex_proto.threading;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


public abstract class AsyncHelperBase implements IAsyncHelper {
    protected final Set<AsyncTaskImpl> activeTasks = new HashSet<>();
    protected final Set<AsyncTaskImpl> activeTasksRead = Collections.unmodifiableSet(activeTasks);

	@Override
	public Set<? extends AsyncTask> getActiveTasks() {
		synchronized (activeTasks) {
			return activeTasksRead;
		}
	}

	@Override
	public AsyncTaskImpl runAsync(Runnable r, String name) {
		AsyncTaskImpl result = new AsyncTaskImpl(r, name);
        invokeTask(result.getTask());
		return result;
	}

	protected abstract void invokeTask(AsyncTaskImpl.Task task);

	@Override
	public AsyncHelperExecutorService createExecutorService(String name) {
		return new AsyncHelperExecutorService(this, name);
	}

	protected abstract void onUncaughtException(Throwable t);
    protected abstract void resetThreadName(Thread t);
    protected abstract void setThreadName(Thread t, String name);

    protected class AsyncTaskImpl extends AsyncTaskBase{
		public AsyncTaskImpl(Runnable runnable, String name) {
			super(runnable, name);
		}

		@Override
		protected void beforeExecution() {
			synchronized (activeTasks) {
				activeTasks.add(this);
			}
		}

		@Override
		protected void afterExecution() {
			synchronized (activeTasks) {
				activeTasks.remove(this);
			}
		}

		@Override
		protected void onUncaughtException(Throwable t){
			AsyncHelperBase.this.onUncaughtException(t);
		}

        @Override
        protected void resetThreadName(Thread t) {
            AsyncHelperBase.this.resetThreadName(t);
        }

        @Override
        protected Task createTask(Runnable runnable, String name) {
            return new Task(runnable, name) {
                public void setName(String name) {
                    if (getThread()!=null) {
                        setThreadName(getThread(), name);
                    }
                    super.setName(name, false);
                }
            };
        }
    }
}
