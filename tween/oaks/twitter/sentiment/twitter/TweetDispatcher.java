package tween.oaks.twitter.sentiment.twitter;

import twitter4j.conf.Configuration;
import twitter4j.internal.async.Dispatcher;
import twitter4j.internal.logging.Logger;

import java.util.LinkedList;
import java.util.List;


public final class TweetDispatcher implements Dispatcher {
    private ExecuteThread[] threads;
    private final List<Runnable> q = new LinkedList<Runnable>();
    private final int LIST_MAX_SIZE = 100;
    private static Logger logger = Logger.getLogger(TweetDispatcher.class);
    private static long skipCount = 0;
    private static long count = 0;

    public TweetDispatcher(Configuration conf) {
        threads = new ExecuteThread[conf.getAsyncNumThreads()];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new ExecuteThread("tween.oaks.twitter.sentiment.Sentiment Async Dispatcher", this, i);
            threads[i].setDaemon(true);
            threads[i].start();
        }
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                if (active) {
                    shutdown();
                }
            }
        });
        logger.info("Right dispatcher");
    }

    public synchronized void invokeLater(Runnable task) {
        synchronized (q) {
            if(count > Integer.MAX_VALUE - 1) {
                logger.info("Counter reset");
                count = 0;
                skipCount = 0;
            }
            if (q.size() >= LIST_MAX_SIZE) {
                q.remove(0);
                skipCount++;
            }
            count++;
            logger.info(count + " tweets; " + skipCount + " skipped; " + 100.0 * skipCount / count);
            q.add(task);
        }
        synchronized (ticket) {
            ticket.notify();
        }
    }

    final Object ticket = new Object();

    public Runnable poll() {
        while (active) {
            synchronized (q) {
                if (q.size() > 0) {
                    Runnable task = q.remove(0);
                    if (task != null) {
                        return task;
                    }
                }
            }
            synchronized (ticket) {
                try {
                    ticket.wait();
                } catch (InterruptedException ignore) {
                }
            }
        }
        return null;
    }

    private boolean active = true;

    public synchronized void shutdown() {
        if (active) {
            active = false;
            for (ExecuteThread thread : threads) {
                thread.shutdown();
            }
            synchronized (ticket) {
                ticket.notify();
            }
        }
    }

    class ExecuteThread extends Thread {
        TweetDispatcher q;

        ExecuteThread(String name, TweetDispatcher q, int index) {
            super(name + "[" + index + "]");
            this.q = q;
        }

        public void shutdown() {
            alive = false;
        }

        private boolean alive = true;

        public void run() {
            while (alive) {
                Runnable task = q.poll();
                if (task != null) {
                    try {
                        task.run();
                    } catch (Exception ex) {
                        logger.error("Got an exception while running a task:", ex);
                    }
                }
            }
        }
    }
}
