package org.concurrency.ratelimiter;

import java.util.concurrent.TimeUnit;

/**
 * Implements the leaky bucket rate limiter algorithm
 */
public class LeakyBucketRateLimiter extends RateLimiter {

    private volatile long lastExecutionTime;
    private final long delay;
    private int possibleTokens = 0;

    public LeakyBucketRateLimiter(int rate, int limit) {
        super(rate, limit);
        lastExecutionTime = System.currentTimeMillis();
        delay = TimeUnit.SECONDS.toMillis(1) / getRate();
    }

    @Override
    public void acquire() {
        synchronized (this) {
            possibleTokens += ((System.currentTimeMillis() - lastExecutionTime) / delay);
            if (possibleTokens > getLimit()) {
                possibleTokens = getLimit();
            }
            if (possibleTokens == 0) {
                try {
                    Thread.sleep(delay - (System.currentTimeMillis() - lastExecutionTime));
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            } else {
                possibleTokens--;
            }
            lastExecutionTime = System.currentTimeMillis();
        }
    }
}
