package org.concurrency.ratelimiter;

public abstract class RateLimiter {
   private final int rate;
   private final int limit;

    public RateLimiter(int rate, int limit) {
        this.rate = rate;
        this.limit = limit;
    }

    public abstract void acquire() throws InterruptedException;

    public int getRate() {
        return rate;
    }

    public int getLimit() {
        return limit;
    }
}
