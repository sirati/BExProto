package de.sirati97.bex_proto.test.v2;

import de.sirati97.bex_proto.threading.AsyncTask;
import de.sirati97.bex_proto.threading.ShutdownBehavior;
import de.sirati97.bex_proto.threading.ThreadPoolAsyncHelper;

import java.util.Set;

/**
 * Created by sirati97 on 07.02.2017 for BexProto.
 */
public class ThreadPoolAsyncHelperTestProgram {

    public static void main(String... args)  throws Throwable {
        final Thread main = Thread.currentThread();
        final ThreadPoolAsyncHelper helper = new ThreadPoolAsyncHelper(ShutdownBehavior.JavaVMShutdownWait);

        helper.runAsync(new Runnable() {
            int count = 5;

            @Override
            public void run() {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(count--);
                if (count > 0) {
                    helper.runAsync(this, "Test" + count);
                } else {
                    Thread t = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Set<? extends AsyncTask> taskSet = helper.getActiveTasks();
                            System.out.println("Active tasks");
                            if (taskSet.size() > 0) {
                                System.out.println("Still active threads: ");
                                for (AsyncTask task:taskSet) {
                                    Thread t = task.getThread();
                                    System.out.println(t.getName() + " state=" + t.getState().toString());
                                }
                            }
                            System.out.println("Active threads");

                            Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
                            for (Thread thread:threadSet) {
                                System.out.println(thread.getId() +" " + thread.getName() + " " + thread.isDaemon());
                            }
                        }
                    });
                    t.setName("Result checker");
                    t.setDaemon(true);
                    t.start();

                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, "Test5");
    }
}
