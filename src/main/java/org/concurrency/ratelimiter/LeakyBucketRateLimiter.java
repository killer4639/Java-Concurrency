package org.concurrency.ratelimiter;

import java.util.concurrent.TimeUnit;

/**
 * Implements the leaky bucket rate limiter algorithm
 */
public class LeakyBucketRateLimiter extends RateLimiter {

    private volatile long lastExecutionTime;
    private final long delay;

    public LeakyBucketRateLimiter(int rate, int limit) {
        super(rate, limit);
        lastExecutionTime = System.currentTimeMillis();
        delay = TimeUnit.SECONDS.toMillis(1) / getRate();
    }

    @Override
    public void acquire() {
        synchronized (this) {
            while ((System.currentTimeMillis() - lastExecutionTime) < delay) ;

            long tokens = (System.currentTimeMillis() - lastExecutionTime) / delay;
            tokens = Math.min(tokens - 1, getLimit());
            lastExecutionTime = System.currentTimeMillis() - tokens * delay;
        }
    }
}
