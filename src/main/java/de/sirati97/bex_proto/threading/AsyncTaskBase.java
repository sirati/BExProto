package de.sirati97.bex_proto.threading;

/**
 * Created by sirati97 on 09.05.2016.
 */
public abstract class AsyncTaskBase implements AsyncTask {
    private final Task task;

    public AsyncTaskBase(Runnable runnable, String name) {
        this.task = new Task(runnable, name);
    }

    @Override
    public void stop() {
        task.stop();
    }

    @Override
    public boolean isRunning() {
        return task.isRunning();
    }

    @Override
    public String getName() {
        return task.getName();
    }

    @Override
    public void setName(String name) {
        task.setName(name);
    }

    protected abstract void beforeExecution();
    protected abstract void afterExecution();
    protected abstract void onUncaughtException(Throwable t);

    protected Task getTask() {
        return task;
    }

    @Override
    public Thread getThread() {
        return task.getThread();
    }

    protected class Task implements Runnable {
        private final Runnable runnable;
        boolean running = false;
        private String name;
        private Thread thread = null;

        public Task(Runnable runnable, String name) {
            this.runnable = runnable;
            this.name = name;
        }

        @Override
        public void run() {
            thread = Thread.currentThread();
            thread.setName(name);
            beforeExecution();
            running = true;
            try {
                runnable.run();
            } catch(Throwable t) {
                onUncaughtException(t);
            } finally {
                running = false;
                afterExecution();
                thread.setName("Thread finished execution. Waiting on next Task.");
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

        public Thread getThread() {
            return thread;
        }
    }
}
