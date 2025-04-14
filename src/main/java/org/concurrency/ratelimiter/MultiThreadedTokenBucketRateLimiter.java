package org.concurrency.ratelimiter;

import java.util.concurrent.TimeUnit;

public class MultiThreadedTokenBucketRateLimiter extends RateLimiter {

    private long tokens;
    private final long delay;

    public MultiThreadedTokenBucketRateLimiter(int rate, int limit) {
        super(rate, limit);
        tokens = 0;
        delay = TimeUnit.SECONDS.toMillis(1) / getRate();

        Runnable tokenFiller = () -> {
            while (true) {
                synchronized (this) {
                    if (tokens < limit) {
                        tokens++;
                        this.notifyAll();
                    }
                }
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    break;
                }
            }
        };

        Thread thread = new Thread(tokenFiller);
        thread.setDaemon(true);
        thread.start();
    }

    @Override
    public void acquire() throws InterruptedException {
        synchronized (this) {
            while (tokens <= 0) {
                this.wait();
            }
            tokens--;
        }
    }
}
